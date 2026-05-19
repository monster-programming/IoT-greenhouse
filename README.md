# 🌿 IoT Smart Greenhouse (Умная Теплица)

![Java](https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2-brightgreen?style=for-the-badge&logo=springboot)
![MQTT](https://img.shields.io/badge/MQTT-Mosquitto-purple?style=for-the-badge&logo=eclipsemosquitto)
![Python](https://img.shields.io/badge/Python-3.10-blue?style=for-the-badge&logo=python)

Система автоматического мониторинга и управления микроклиматом умной теплицы на базе реактивного взаимодействия Spring Boot и физических IoT-устройств по протоколу MQTT.

---

## 🚀 Основные возможности (Features)

* **Реактивное управление:** Автоматическое открытие/закрытие окон при выходе температуры за критические рамки (архитектурный паттерн **Visitor**).
* **Архитектурная связанность:** Полная синхронизация состояния устройств в СУБД (PostgreSQL/H2) с реальным железом через брокер сообщений.
* **Эмуляция железа:** Наличие динамического Python-скрипта с симуляцией обратной связи (Feedback Loop) для тестирования системы без физической платы.

---

## 📐 Архитектура системы

Проект построен на базе событийно-ориентированной архитектуры:
1. Датчики (Python-скрипт) публикуют телеметрию в топик `greenhouse/sensors`.
2. Бэкенд (Spring Integration) считывает данные, обновляет БД и передает объект на обработку бизнес-логике.
3. В случае критических показателей, Спринг через `MqttGateway` отправляет команду в топик `greenhouse/actuators/`.

---

## 🛠 Стек технологий

* **Backend:** Java 17, Spring Boot 3.x, Spring Data JPA, Spring Integration MQTT
* **Broker:** Eclipse Mosquitto (https://mosquitto.org/download/)
* **Hardware/Emulation:** Python 3 (paho-mqtt)
* **Database:** H2 

---

## ⚙️ Запуск и развертывание

### 1. Требования
* JDK 17+
* Брокер Mosquitto (запущен на локальном порту 1883)

### 2. Запуск
* Запуск Mosquitto
* Запуск сервера
* Запуск скрипта

### 3. Полезная информация
* При запуске сервера должны выестись сообщения о том, что база данных проинициализирована. Сделано для того, чтобы изначально на севере уже лежала датчик. Сейчас в него попадает датчик температуры.

