package ru.hogwarts.school.constants;

import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
public class Constants {
    public static final Student POTTER = new Student(1L, "Гарри Поттер", 12, new Faculty(1L, "Гриффиндор", "красный"));
    public static final Student POLUMNA = new Student(2L, "Полумна Лавгуд", 12, new Faculty(2L, "Когтевран", "синий"));
    public static final Student CHANG = new Student(3L, "Чжоу Чанг", 13, new Faculty(1L, "Гриффиндор", "красный"));
    public static final Student DIGGORY = new Student(4L, "Седрик Диггори", 14, new Faculty(3L, "Слизерин", "зеленый"));
    public static final Student WISLY = new Student(5L, "Рон Уизли", 12, new Faculty(4L, "Пуффендуй", "желтый"));
    public static final Student GREINDGER = new Student(6L, "Гермиона Грейнджер", 11, new Faculty(4L, "Пуффендуй", "желтый"));

    public static final Faculty GRIFFINDOR = new Faculty(1L, "Гриффиндор", "красный");
    public static final Faculty PUFFENDUY = new Faculty(4L, "Пуффендуй", "желтый");
    public static final Faculty SLIZERIN = new Faculty(3L, "Слизерин", "зеленый");
    public static final Faculty COGTEVRAN = new Faculty(2L, "Когтевран", "синий");
}
