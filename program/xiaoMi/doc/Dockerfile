# 使用官方的Java运行时作为父镜像
FROM openjdk:8

# 设置工作目录
WORKDIR /app

# 将本地jar包复制到容器中
COPY target/xiaomi-0.0.1-SNAPSHOT.jar app.jar

# 设置容器启动时执行的命令
ENTRYPOINT ["java","-jar","/app/app.jar"]

# 暴露应用端口
EXPOSE 8080