package work;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class OrderProcessor {
    private static List<Order> orderList = new ArrayList<>();
    private static Map<String, OrderStatistics> statisticsMap = new HashMap<>();
    private static final int BATCH_SIZE = 1000;
    private static ExecutorService threadPool = Executors.newFixedThreadPool(10);

    public static void processOrder(Order order) {
        synchronized (orderList) {
            orderList.add(order);
            if (orderList.size() >= BATCH_SIZE) {
                threadPool.submit(() -> processBatch(orderList));
                orderList = new ArrayList<>();
            }
        }

        // 更新订单统计信息
        updateStatistics(order);
    }

    private static void processBatch(List<Order> batch) {
        try {
            // 模拟批处理逻辑，耗时操作
            Thread.sleep(500);

            // 模拟数据库操作
            for (Order order : batch) {
                saveToDatabase(order);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void updateStatistics(Order order) {
        String category = order.getCategory();
        statisticsMap.computeIfAbsent(category, k -> new OrderStatistics())
                .addOrder(order);
    }

    private static void saveToDatabase(Order order) {
        // 模拟数据库操作
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        long startTime = System.currentTimeMillis();

        // 模拟高并发订单处理
        ExecutorService testExecutor = Executors.newFixedThreadPool(50);
        for (int i = 0; i < 10000; i++) {
            final int orderNum = i;
            testExecutor.submit(() -> {
                Order order = new Order("ORD" + orderNum,
                        Math.random() * 1000,
                        "Category" + (orderNum % 5));
                processOrder(order);
            });
        }

        testExecutor.shutdown();
        testExecutor.awaitTermination(1, TimeUnit.MINUTES);

        // 关闭线程池
        threadPool.shutdown();
        threadPool.awaitTermination(1, TimeUnit.MINUTES);

        System.out.println("处理完成，耗时: " + (System.currentTimeMillis() - startTime) + "ms");
        System.out.println("订单总数: " + orderList.size());
        System.out.println("统计信息: " + statisticsMap.size() + " 个分类");
    }
}

class Order {
    private String orderId;
    private double amount;
    private String category;
    private byte[] imageData; // 订单图片数据

    public Order(String orderId, double amount, String category) {
        this.orderId = orderId;
        this.amount = amount;
        this.category = category;
        this.imageData = new byte[1024 * 10]; // 10KB图片数据
    }
    public double getAmount() {
        return amount;
    }
    public String getCategory() {
        return category;
    }

    public String getOrderId() {
        return orderId;
    }
}

class OrderStatistics {
    private int count;
    private double totalAmount;
    private List<Order> recentOrders = new ArrayList<>();

    public synchronized void addOrder(Order order) {
        count++;
        totalAmount += order.getAmount();
        recentOrders.add(order);

        // 只保留最近100个订单
        if (recentOrders.size() > 100) {
            recentOrders.remove(0);
        }
    }
}