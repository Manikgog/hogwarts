--Шаг 1

--С прошлых уроков у нас есть две таблицы: Student и Faculty. Необходимо для них создать следующие ограничения:

-- Возраст студента не может быть меньше 16 лет.
-- Имена студентов должны быть уникальными и не равны нулю.
-- Пара “значение названия” - “цвет факультета” должна быть уникальной.
-- При создании студента без возраста ему автоматически должно присваиваться 20 лет.

ALTER TABLE public.students ADD CONSTRAINT age_constraint CHECK(age > 16)

ALTER TABLE public.students ADD CONSTRAINT name__unique UNIQUE(name)

ALTER TABLE public.students ALTER COLUMN name SET NOT NULL

ALTER TABLE public.faculties ADD CONSTRAINT unique_color_and_name UNIQUE (color, name)

ALTER TABLE public.students ALTER COLUMN age SET DEFAULT 20
