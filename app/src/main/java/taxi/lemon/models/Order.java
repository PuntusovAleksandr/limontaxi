package taxi.lemon.models;

import java.util.ArrayList;

/**
 * Order holder
 */
public class Order {
    private static Order ourInstance;

    public static void initInstance() {
        ourInstance = new Order();
    }

    public static Order getInstance() {
        return ourInstance;
    }

    public static final int RATE_BASE = 1;
    public static final int RATE_PREMIUM = 2;
    public static final int RATE_TRUCK = 3;
    public static final int RATE_WAGON = 4;
    public static final int RATE_MINIBUS = 5;
    public static final int RATE_BUSINESS = 6;

    private boolean conditioner;
    private boolean animals;
    private boolean delivery;
    private boolean baggage;
//    private boolean meetWithTable;
    private boolean emptyTrunk;
    private boolean routeUndefined;
    private boolean terminalPay;
    private boolean preOrder;
    private boolean receiptNeed;

    private int rate;

    private long distance;

    private Double cost;

    private long addCost;

    private String comment;

    private String orderDate;

    private String orderTime;

    private ArrayList<RouteItem> route;

    private String phone;

    private String encodedRoute;

//    private Integer cityId;

    private Order() {
        rate = RATE_BASE;
        comment = "";
        route = new ArrayList<>();
//        cityId = null;
        cost = null;
    }

    public boolean isReceiptNeed() {
        return receiptNeed;
    }

    public void setReceiptNeed(boolean receiptNeed) {
        this.receiptNeed = receiptNeed;
    }

    public boolean isTerminalPay() {
        return terminalPay;
    }

    public void setTerminalPay(boolean terminalPay) {
        this.terminalPay = terminalPay;
    }

    public boolean isRouteUndefined() {
        return routeUndefined;
    }

    public void setRouteUndefined(boolean routeUndefined) {
        this.routeUndefined = routeUndefined;
    }

//    public Integer getCityId() {
//        return cityId;
//    }

//    public void setCityId(Integer cityId) {
//        this.cityId = cityId;
//    }

    public boolean isEmptyTrunk() {
        return emptyTrunk;
    }

    public void setEmptyTrunk(boolean emptyTrunk) {
        this.emptyTrunk = emptyTrunk;
    }

    public void updateInstance(Order order) {
        ourInstance = order;
    }

    public boolean isConditioner() {
        return conditioner;
    }

    public void setConditioner(boolean conditioner) {
        this.conditioner = conditioner;
    }

    public boolean isAnimals() {
        return animals;
    }

    public void setAnimals(boolean animals) {
        this.animals = animals;
    }

    public boolean isDelivery() {
        return delivery;
    }

    public void setDelivery(boolean delivery) {
        this.delivery = delivery;
    }

    public boolean isBaggage() {
        return baggage;
    }

    public void setBaggage(boolean baggage) {
        this.baggage = baggage;
    }

//    public boolean isMeetWithTable() {
//        return meetWithTable;
//    }

//    public void setMeetWithTable(boolean meetWithTable) {
//        this.meetWithTable = meetWithTable;
//    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public long getDistance() {
        return distance;
    }

    public void setDistance(long distance) {
        this.distance = distance;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public long getAddCost() {
        return addCost;
    }

    public void setAddCost(long addCost) {
        this.addCost = addCost;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isPreOrder() {
        return preOrder;
    }

    public void setPreOrder(boolean preOrder) {
        this.preOrder = preOrder;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public void addRouteItem(RouteItem item) {
        route.add(item);
    }

    public void updateRouteItem(int index, RouteItem item) {
        route.set(index, item);
    }

    public void setRoute(ArrayList<RouteItem> route) {
        this.route = route;
    }

    public ArrayList<RouteItem> getRoute() {
        return route;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPreOrderTime() {
        if (orderDate != null && orderTime != null) {
            return getOrderDate() + " " + getOrderTime();
        } else {
            return "";
        }
    }

    public String getEncodedRoute() {
        return (encodedRoute == null) ? "" : encodedRoute;
    }

    public void setEncodedRoute(String encodedRoute) {
        this.encodedRoute = encodedRoute;
    }

    public void resetOrder() {
        route = new ArrayList<>();
        rate = RATE_BASE;
        conditioner = false;
        animals = false;
        delivery = false;
        baggage = false;
//        meetWithTable = false;
        emptyTrunk = false;
        routeUndefined = false;
        terminalPay = false;
        preOrder = false;
        receiptNeed = false;
        distance = 0;
        cost = null;
        addCost = 0;
        comment = "";
        orderDate = null;
        orderTime = null;
        encodedRoute = null;
    }
}
