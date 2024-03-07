package ru.hogwarts.school.constants;

import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.entity.Student;
public class Constants {
    public static final Student POTTER = new Student();
    public static final Student POLUMNA = new Student();
    public static final Student CHANG = new Student();
    public static final Student DIGGORY = new Student();
    public static final Student WISLY = new Student();
    public static final Student GREINDGER = new Student();

    public static final Faculty GRIFFINDOR = new Faculty();
    public static final Faculty PUFFENDUY = new Faculty();
    public static final Faculty SLIZERIN = new Faculty();
    public static final Faculty COGTEVRAN = new Faculty();

    static{
        GRIFFINDOR.setId(1L);
        GRIFFINDOR.setColor("красный");
        GRIFFINDOR.setName("Гриффиндор");

        PUFFENDUY.setId(4L);
        PUFFENDUY.setColor("желтый");
        PUFFENDUY.setName("Пуффендуй");

        SLIZERIN.setId(3L);
        SLIZERIN.setColor("зеленый");
        SLIZERIN.setName("Слизерин");

        COGTEVRAN.setId(2L);
        COGTEVRAN.setColor("синий");
        COGTEVRAN.setName("Когтевран");

        POTTER.setId(0L);
        POTTER.setName("Гарри Поттер");
        POTTER.setAge(14);
        POTTER.setFaculty(GRIFFINDOR);

        GREINDGER.setId(0L);
        GREINDGER.setAge(14);
        GREINDGER.setName("Гермиона Грейнджер");
        GREINDGER.setFaculty(GRIFFINDOR);

        WISLY.setId(0L);
        WISLY.setAge(15);
        WISLY.setName("Рон Уизли");
        WISLY.setFaculty(GRIFFINDOR);

    }
}
