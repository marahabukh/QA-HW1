package test.java;

import main.java.ProductStock;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public  class ProductStockTest
{
    private ProductStock stock;

    @BeforeEach
    void setUp() {
        stock = new ProductStock("DEFAULT_PRODUCT_ID", "DEFAULT_LOCATION", 10, 3, 100);
    }

    @Test
    @DisplayName("addStockNormal")
    @Tag("sanity")
    public void addStockNormal() {

        stock.addStock(20);
        assertEquals(30, stock.getOnHand());
    }
    @Test
    public void addStockBoundary() {

        stock.addStock(90);
        assertEquals(100,stock.getOnHand() );
    }
    @Test
    public void addStockOverCapicity() {

        assertThrows(IllegalStateException.class, () -> stock.addStock(110));
    }

    @Test
    public void addStockError() {

        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> stock.addStock(0)),
                () -> assertThrows(IllegalArgumentException.class, () -> stock.addStock(-5))
        );}
    @Test
    @DisplayName("Normal")
    @Tag("sanity")
    public void reserveNormal() {
        stock.reserve(5);
        assertEquals(5, stock.getReserved());
        assertEquals(5, stock.getAvailable());
    }
    @Test
    public void reserveBoundary() {

        stock.reserve(10);
        assertEquals(10, stock.getReserved());
        assertEquals(0, stock.getAvailable());
    }
    @Test
    public void reservnotvalid() {

        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> stock.reserve(0)),
                () -> assertThrows(IllegalArgumentException.class, () -> stock.reserve(-3))
        );
    }
    @Test
        public void reserveover() {
            assertThrows(IllegalStateException.class, () -> stock.reserve(20));
        }


    @Test
    @DisplayName("releaseReservationValid")
    @Tag("sanity")
    public void releaseReservationNormal() {
        stock.reserve(8);
        stock.releaseReservation(3);

        assertEquals(5, stock.getReserved());
    }
    @Test
    public void releaseReservationBoundary() {
        stock.reserve(8);
        stock.releaseReservation(8);

        assertEquals(0, stock.getReserved());
    }
    @Test
    public void releaseReservationInvalid() {
        stock.reserve(5);

        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> stock.releaseReservation(0)),
                () -> assertThrows(IllegalArgumentException.class, () -> stock.releaseReservation(-2))
        );
    }
    @Test
    public void releaseReservationOverAvailable() {
        stock.reserve(5);

        assertThrows(IllegalStateException.class, () -> stock.releaseReservation(10));
    }
    @Test
    @DisplayName("shipReserved Normal")
    public void shipReservedNormal() {

        stock.reserve(8);
        stock.shipReserved(5);

        assertEquals(3, stock.getReserved());
        assertEquals(5, stock.getOnHand());
    }
    @Test
    public void shipReservedBoundary() {

        stock.reserve(7);
        stock.shipReserved(7);

        assertEquals(0, stock.getReserved());
        assertEquals(3, stock.getOnHand());
    }
    @Test
    public void shipReservedInvalidAmount() {

        stock.reserve(5);

        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> stock.shipReserved(0)),
                () -> assertThrows(IllegalArgumentException.class, () -> stock.shipReserved(-2))
        );
    }
    @Test
    public void shipReservedOverReserved() {

        stock.reserve(3);

        assertThrows(IllegalStateException.class, () -> stock.shipReserved(5));
    }
    @Test
    public void shipReservedExceedOnHand() {

        stock = new ProductStock("P1", "L1", 5, 1, 100);

        stock.reserve(5);

        assertThrows(IllegalStateException.class, () -> stock.shipReserved(6));
    }
    @Test
    @DisplayName("removeDamaged Normal")
    public void removeDamagedNormal() {
        stock.removeDamaged(3);
        assertEquals(7, stock.getOnHand());
    }
    @Test
    public void removeDamagedBoundary() {
        stock.removeDamaged(10);
        assertEquals(0, stock.getOnHand());
    }
    @Test
    public void removeDamagedInvalid() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> stock.removeDamaged(0)),
                () -> assertThrows(IllegalArgumentException.class, () -> stock.removeDamaged(-5))
        );
    }
    @Test
    public void removeDamagedOverOnHand() {
        assertThrows(IllegalStateException.class, () -> stock.removeDamaged(20));
    }
    @Test
    public void removeDamagedAdjustReserved() {

        stock.reserve(8);
        stock.removeDamaged(5);

        assertEquals(5, stock.getReserved());
    }
    @Test
    @DisplayName("isReorderNeededNormal")
    public void reorderNeededTrue() {

        stock.reserve(8);

        assertTrue(stock.isReorderNeeded());
    }
    @Test
    public void reorderNeededEqual() {
        stock.reserve(7);
        assertFalse(stock.isReorderNeeded());
    }
    @Test
    public void reorderNotNeeded() {
        stock.reserve(5);
        assertFalse(stock.isReorderNeeded());
    }
    @Test
    @DisplayName("updateReorderThresholdNormal")
    public void updateThresholdValid() {
        stock.updateReorderThreshold(5);
        assertEquals(5, stock.getReorderThreshold());
    }
    @Test
    public void updateThresholdBoundary() {
        stock.updateReorderThreshold(100);
        assertEquals(100, stock.getReorderThreshold());
    }

    @Test
    public void updateThresholdNegative() {
        assertThrows(IllegalArgumentException.class,
                () -> stock.updateReorderThreshold(-1));
    }

    @Test
    public void updateThresholdOverCapacity() {
        assertThrows(IllegalArgumentException.class,
                () -> stock.updateReorderThreshold(150));
    }
    @Test
    @DisplayName("updateMaxCapacity  valid")
    public void updateMaxCapacityValid() {
        stock.updateMaxCapacity(200);

        assertEquals(200, stock.getMaxCapacity());
    }
    @Test
    public void updateMaxCapacityInvalid() {
        assertThrows(IllegalArgumentException.class,
                () -> stock.updateMaxCapacity(0));

        assertThrows(IllegalArgumentException.class,
                () -> stock.updateMaxCapacity(-5));
    }
    @Test
    public void updateMaxCapacityBelowOnHand() {
        assertThrows(IllegalStateException.class,
                () -> stock.updateMaxCapacity(5));
    }

    @Test
    public void updateMaxCapacityboundary() {
        stock.updateMaxCapacity(10);

        assertEquals(10, stock.getMaxCapacity());
    }

    @Test
    @DisplayName("changeLocation Valid")
    public void changeLocationValid() {
        stock.changeLocation("A-5");
        assertEquals("A-5", stock.getLocation());
    }
    @Test
    public void changeLocationNullOrBlunk() {
        assertThrows(IllegalArgumentException.class,
                () -> stock.changeLocation(null));
        assertThrows(IllegalArgumentException.class,
                () -> stock.changeLocation("   "));
    }
    @Test
    public void consInvalidId() {
        assertThrows(IllegalArgumentException.class,
                () -> new ProductStock(null, "L1", 10, 3, 100));
    }

    @Test
    public void consInvalidLocation() {
        assertThrows(IllegalArgumentException.class,
                () -> new ProductStock("P1", "   ", 10, 3, 100));
    }

    @Test
    public void consInvalidOnHand() {
        assertThrows(IllegalArgumentException.class,
                () -> new ProductStock("P1", "L1", -1, 3, 100));
    }

    @Test
    public void constNegativeThreshold() {
        assertThrows(IllegalArgumentException.class,
                () -> new ProductStock("P1", "L1", 10, -5, 100));
    }

    @Test
    public void consInvalidCapacity() {
        assertThrows(IllegalArgumentException.class,
                () -> new ProductStock("P1", "L1", 10, 3, 0));
    }

    @Timeout(1)
    public void quickTest() {
        assertTrue(true);
    }

    @Test
    public void constOnHandExceedsCapacity() {
        assertThrows(IllegalArgumentException.class,
                () -> new ProductStock("P1", "L1", 200, 3, 100));
    }

    @Test
    public void constValid() {
        ProductStock p = new ProductStock("P1", "L1", 10, 3, 100);

        assertAll(
                () -> assertEquals("P1", p.getProductId()),
                () -> assertEquals("L1", p.getLocation()),
                () -> assertEquals(10, p.getOnHand()),
                () -> assertEquals(3, p.getReorderThreshold()),
                () -> assertEquals(100, p.getMaxCapacity())
        );
    }
    @Test
    @Timeout(2)
    @DisplayName("shipReserved with timeout")
    public void shipReservedWithTimeout() throws InterruptedException {

        stock.reserve(5);
        Thread.sleep(200);
        stock.shipReserved(3);
        assertEquals(2, stock.getReserved());
        assertEquals(7, stock.getOnHand());
    }

    @Test
    @Disabled("FutureTest")
    public void FutureTest()  {

    }











}








