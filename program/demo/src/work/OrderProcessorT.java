package work;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

public class OrderProcessorT {
    private static final int BATCH_SIZE = 1000;
    private static final BlockingQueue<OrderT> queue = new LinkedBlockingQueue<>();
    private static final Map<String, OrderStatisticsT> statisticsMap = new ConcurrentHashMap<>();

    private static final ThreadPoolExecutor batchProcessor = new ThreadPoolExecutor(
            4,
            10,
            60, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(50), // 有界队列，避免内存爆炸
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.AbortPolicy() // 拒绝策略
    );

    static {
        // 启动一个独立线程定时从队列中消费
        Thread consumerThread = new Thread(() -> {
            List<OrderT> batch = new ArrayList<>(BATCH_SIZE);
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    OrderT order = queue.poll(1, TimeUnit.SECONDS);
                    if (order != null) batch.add(order);

                    if (batch.size() >= BATCH_SIZE || (order == null && !batch.isEmpty())) {
                        final List<OrderT> toProcess = new ArrayList<>(batch);
                        batch.clear();
                        try {
                            batchProcessor.execute(() -> processBatch(toProcess));
                        } catch (RejectedExecutionException e) {
                            System.err.println("batch 完成");
                        }
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        consumerThread.setDaemon(true);
        consumerThread.start();
    }

    public static void processOrder(OrderT order) {
        queue.offer(order);
        updateStatistics(order);
    }

    private static void processBatch(List<OrderT> batch) {
        try {
            Thread.sleep(500); // 模拟处理
            for (OrderT order : batch) {
                saveToDatabase(order);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static void updateStatistics(OrderT order) {
        statisticsMap.computeIfAbsent(order.getCategory(), k -> new OrderStatisticsT())
                .addOrder(order);
    }

    private static void saveToDatabase(OrderT order) {
        try {
            Thread.sleep(10); // 模拟写库
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        long start = System.currentTimeMillis();

        ExecutorService testExecutor = Executors.newFixedThreadPool(50);
        for (int i = 0; i < 10000; i++) {
            final int orderNum = i;
            testExecutor.submit(() -> {
                OrderT orderT = new OrderT("ORD" + orderNum,
                        Math.random() * 1000,
                        "Category" + (orderNum % 5));
                processOrder(orderT);
            });
        }

        testExecutor.shutdown();
        testExecutor.awaitTermination(2, TimeUnit.MINUTES);

        batchProcessor.shutdown();
        batchProcessor.awaitTermination(2, TimeUnit.MINUTES);

        System.out.println("处理完成，耗时: " + (System.currentTimeMillis() - start) + "ms");
        System.out.println("统计信息: " + statisticsMap.size() + " 个分类");
    }
}

class OrderT {
    private String orderId;
    private double amount;
    private String category;

    public OrderT(String orderId, double amount, String category) {
        this.orderId = orderId;
        this.amount = amount;
        this.category = category;
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

class OrderStatisticsT {
    private LongAdder count = new LongAdder();
    private DoubleAdder totalAmount = new DoubleAdder();
    private Deque<OrderT> recentOrders = new ConcurrentLinkedDeque<>();

    public void addOrder(OrderT order) {
        count.increment();
        totalAmount.add(order.getAmount());
        recentOrders.addLast(order);
        if (recentOrders.size() > 100) {
            recentOrders.pollFirst();
        }
    }
}
