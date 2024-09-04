# Телеграмм бот с расписанием
## Основные функции 

### **Получение и вывод расписания занятий для студентов Самарского университета**

- Должен быть вывод расписания на выбранную дату, выбранного курса, факультета и группы
- Необходимо реализовать это так, чтобы бот выводил сообщение с расписанием на сегодня и под сообщением иметь инлайн кнопки в количестве двух(или более если придумаю что они должны делать) кнопками для переключения дней. При нажатии на эти кнопки день будет меняться на +-1

### **Регистрация пользователей для получения персонального расписания** 

- Пользователь должен регистрироваться в боте, чтобы получать расписание
	- Выбор факультета
	- Выбора курса
	- Выбор группы

### **Парсинг расписания с официального сайта Самарского университета(Сайтик не имеет апи....)**

- Раз в час должен проводится парсинг расписания только для тех групп, в которых существуют пользователи бота
- После парсинга предметы должны записываться в бд и проверяться с существующими, чтобы находить разницу в расписании

### **Уведомления бота**
- Ежедневные уведомления для пользователей с расписанием на день
- Еженедельные уведомления для пользователей с раписанием на неделю
- Уведомления для изменения расписания. Изменения должны искаться раз в час
- Настройки уведомлений для пользователя
	- Вкл/выкл ежедневные уведомления
	- Вкл/выкл еженедельные уведомления
	- Вкл/выкл уведомлений об изменениях в расписании

### **Административная панель**

- Просмотр текущего количества зарегистрированных пользователей
- Возможность писать от лица бота всем пользователям сообщения(патчноуты бота например...)

### **Интеграции**

- Gismeteo API для получения погоды на день для вывода в дневное расписание
- Генерация .ics для интеграции с гугл календарем (да и вообще любым на самом деле)
