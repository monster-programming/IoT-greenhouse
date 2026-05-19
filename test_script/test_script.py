import time
import json
import random
import threading
import paho.mqtt.client as mqtt

BROKER = "localhost"
PORT = 1883
TOPIC_SENSORS = "greenhouse/sensors"
TOPIC_ACTUATORS = "greenhouse/actuators/+" 

CURR_TEMP = 25.0
is_window_open = False

def on_connect(client, userdata, flags, rc):
    if rc == 0:
        print("[PYTHON] Успешно подключено к MQTT брокеру")
        client.subscribe(TOPIC_ACTUATORS)
        print(f"[PYTHON] Подписано на топик: {TOPIC_ACTUATORS}")
    else:
        print(f"[PYTHON] Не удалось подключиться, код ошибки: {rc}")

def on_message(client, userdata, msg):
    print(f"[PYTHON] Получено сообщение: {msg.topic} -> {msg.payload.decode()}")
    try:
        data = json.loads(msg.payload.decode("utf-8"))
        action = data.get("action")
        if action == "OPEN":
            global is_window_open
            is_window_open = True
            print("[PYTHON] Окно открыто")
        elif action == "CLOSE":
            is_window_open = False
            print("[PYTHON] Окно закрыто")

        print(f"[PYTHON] Десериализованные данные: {data}")
    except json.JSONDecodeError as e:
        print(f"[PYTHON] Ошибка при десериализации JSON: {e}")

client = mqtt.Client(client_id="PythonTestClient")
client.on_connect = on_connect
client.on_message = on_message

client.connect(BROKER, PORT, 60)
client.loop_start()

print("\n" + "="*60)
print(" ЭМУЛЯТОР ТЕПЛИЦЫ С ОБРАТНОЙ СВЯЗЬЮ ЗАПУЩЕН")
print("="*60)

try:
    while True:
        weather_fluction = random.uniform(-0.3, 0.5)
        if is_window_open:
            CURR_TEMP += weather_fluction - 1.2
        else:
            CURR_TEMP += weather_fluction + 0.4

        CURR_TEMP = round(CURR_TEMP, 1)

        payload = {
            "id": "temp_sensor_1",
            "type": "TEMPERATURE",
            "val": CURR_TEMP
        }

        status = "🟢 ОКНО ОТКРЫТО (Охлаждение)" if is_window_open else "🔴 ОКНО ЗАКРЫТО (Прогрев)"
        print(f"[PYTHON] Текущая температура: {CURR_TEMP}°C | {status}")

        client.publish(TOPIC_SENSORS, json.dumps(payload))
        time.sleep(5)
except KeyboardInterrupt:
    print("\n[PYTHON] Завершение работы эмулятора...")
    client.loop_stop()
    client.disconnect()