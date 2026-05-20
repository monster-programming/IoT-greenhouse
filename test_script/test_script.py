import time
import json
import random
import paho.mqtt.client as mqtt

BROKER = "localhost"
PORT = 1883
TOPIC_SENSORS = "greenhouse/sensors"
TOPIC_ACTUATORS = "greenhouse/actuators/+"

# --- Текущие показатели среды ---
CURR_TEMP = 25.0
CURR_HUM = 50.0
CURR_LIGHT = 50

# --- Состояния механизмов ---
is_window_open = False
is_pump_on = False
is_lamp_on = False

def on_connect(client, userdata, flags, rc):
    if rc == 0:
        print("[PYTHON] Успешно подключено к MQTT брокеру")
        client.subscribe(TOPIC_ACTUATORS)
        print(f"[PYTHON] Подписано на топик: {TOPIC_ACTUATORS}")
    else:
        print(f"[PYTHON] Не удалось подключиться, код ошибки: {rc}")

def on_message(client, userdata, msg):
    global is_window_open, is_pump_on, is_lamp_on

    # Получаем ID устройства из топика (например, window_1, pump_1, lamp_1)
    actuator_id = msg.topic.split("/")[-1]

    try:
        data = json.loads(msg.payload.decode("utf-8"))
        action = data.get("action")

        # Логика команд для Окна
        if actuator_id == "window_1":
            if action == "OPEN":
                is_window_open = True
                print("[PYTHON] 💨 Окно открыто (Охлаждение)")
            elif action == "CLOSE":
                is_window_open = False
                print("[PYTHON] 🚪 Окно закрыто (Прогрев)")

        # Логика команд для Помпы
        elif actuator_id == "pump_1":
            if action == "TURN_ON":
                is_pump_on = True
                print("[PYTHON] 💧 Помпа включена (Увлажнение)")
            elif action == "TURN_OFF":
                is_pump_on = False
                print("[PYTHON] 🛑 Помпа выключена (Сушка)")

        # Логика команд для Лампы
        elif actuator_id == "lamp_1":
            if action == "TURN_ON":
                is_lamp_on = True
                print("[PYTHON] 💡 Лампа включена (Светлеет)")
            elif action == "TURN_OFF":
                is_lamp_on = False
                print("[PYTHON] 🌑 Лампа выключена (Темнеет)")

    except json.JSONDecodeError as e:
        print(f"[PYTHON] Ошибка при десериализации JSON: {e}")

client = mqtt.Client(client_id="PythonTestClient")
client.on_connect = on_connect
client.on_message = on_message

client.connect(BROKER, PORT, 60)
client.loop_start()

print("\n" + "="*70)
print(" РОСКОШНЫЙ ЭМУЛЯТОР ТЕПЛИЦЫ С ОБРАТНОЙ СВЯЗЬЮ ЗАПУЩЕН")
print("="*70)

try:
    while True:
        # --- 1. ФИЗИКА ТЕМПЕРАТУРЫ ---
        temp_fluctuation = random.uniform(-0.3, 0.5)
        if is_window_open:
            CURR_TEMP += temp_fluctuation - 1.5 # Остывает при открытом окне
        else:
            CURR_TEMP += temp_fluctuation + 0.5 # Нагревается при закрытом
        CURR_TEMP = round(CURR_TEMP, 1)

        # --- 2. ФИЗИКА ВЛАЖНОСТИ ---
        hum_fluctuation = random.uniform(-1.0, 1.0)
        if is_pump_on:
            CURR_HUM += hum_fluctuation + 4.0 # Быстро увлажняется от помпы
        else:
            CURR_HUM += hum_fluctuation - 1.5 # Постепенно сохнет
        CURR_HUM = max(0.0, min(100.0, round(CURR_HUM, 1))) # Держим в рамках 0-100%

        # --- 3. ФИЗИКА СВЕТА ---
        light_fluctuation = random.randint(-5, 5)
        if is_lamp_on:
            CURR_LIGHT += light_fluctuation + 15 # Становится светло от лампы
        else:
            CURR_LIGHT += light_fluctuation - 8 # Постепенно темнеет (вечереет)
        CURR_LIGHT = max(0, min(100, int(CURR_LIGHT))) # Держим в рамках 0-100%

        # --- ФОРМИРОВАНИЕ JSON ПАКЕТОВ ---
        temp_payload = {"id": "temp_sensor_1", "type": "TEMPERATURE", "val": CURR_TEMP}
        hum_payload = {"id": "hum_sensor_1", "type": "HUMIDITY", "val": CURR_HUM}
        light_payload = {"id": "light_sensor_1", "type": "LIGHT", "val": CURR_LIGHT}

        # Красивый вывод в консоль
        w_status = "🟢" if is_window_open else "🔴"
        p_status = "🟢" if is_pump_on else "🔴"
        l_status = "🟢" if is_lamp_on else "🔴"

        print(f"[СЕНСОРЫ] Темп: {CURR_TEMP}°C [{w_status}] | Влажн: {CURR_HUM}% [{p_status}] | Свет: {CURR_LIGHT}% [{l_status}]")

        # --- ОТПРАВКА ДАННЫХ ---
        client.publish(TOPIC_SENSORS, json.dumps(temp_payload))
        client.publish(TOPIC_SENSORS, json.dumps(hum_payload))
        client.publish(TOPIC_SENSORS, json.dumps(light_payload))

        time.sleep(4)

except KeyboardInterrupt:
    print("\n[PYTHON] Завершение работы эмулятора...")
    client.loop_stop()
    client.disconnect()