CommandTask
===========

Конфиг:
<pre>
# Таблица с полями: cid (int), command (varchar), timestamp (timestamp)
connect:
    ip: 127.0.0.1
    db: localhost
    login: root
    password: password
    table: commands
ticks:
    update: 20
    interval: 10
</pre>

Команды:<br />
<code>commandtask reload</code> - перезагрузить плагин