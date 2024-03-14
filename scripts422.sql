-- Шаг 2

-- В этом задании по описанию необходимо спроектировать таблицы, связи между ними и корректно определить типы данных.
-- Здесь не важно, какой тип вы выберете, например, для данных, представленных в виде строки (varchar или text).
-- Важно, что вы выберете один из строковых типов, а не числовых, например.

-- Описание структуры: у каждого человека есть машина. Причем несколько человек могут пользоваться одной машиной.
-- У каждого человека есть имя, возраст и признак того, что у него есть права (или их нет). У каждой машины есть
-- марка, модель и стоимость. Также не забудьте добавить таблицам первичные ключи и связать их.

CREATE TABLE automobiles (
	Id SERIAL UNIQUE,
	Brand TEXT NOT NULL,
	Model TEXT NOT NULL,
	Price MONEY DEFAULT(0) NOT NULL
)

CREATE TABLE humans (
	Id SERIAL UNIQUE,
	Name TEXT NOT NULL,
	Age INTEGER NOT NULL CONSTRAINT age_constraint CHECK(Age > 0),
	IsDriverLicense BOOL DEFAULT False,
	Automobile_Id INTEGER NOT NULL REFERENCES automobiles (id)
)
