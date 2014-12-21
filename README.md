CommandTask
===========

Конфиг:
<pre>
# Таблица с полями: cid (int), command (varchar), timestamp (timestamp)
connect:
    enabled: false
    ip: 127.0.0.1
    db: localhost
    login: root
    password: password
    table: commands

# Для обработки заданий в БД
ticks:
    update: 40
    execute: 10

# Формат файла:
#  s m h dom mon dow command
# где:
#  s - секунда
#  m - минута
#  h - час
#  dom - день
#  mon - месяц
#  dow - день недели (1 - вс, ... 7 - сб)
#  command - команда выполняемая от имени консоли
crontab:
    enabled: false
    filename: tasks.txt
</pre>

Команды:<br />
<code>commandtask reload</code> - перезагрузить плагин
