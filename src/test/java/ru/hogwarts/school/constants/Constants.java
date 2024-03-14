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
    public static final Student BELL = new Student();
    public static final Student BING = new Student();
    public static final Student JOE = new Student();
    public static final Student ROSS = new Student();
    public static final Student MONICA = new Student();
    public static final Student RACHEL = new Student();
    public static final Student FUIBY = new Student();
    public static final Faculty FACULTY_1 = new Faculty();
    public static final Faculty FACULTY_2 = new Faculty();
    public static final Faculty FACULTY_3 = new Faculty();
    public static final Faculty FACULTY_4 = new Faculty();

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

        FACULTY_1.setId(0L);
        FACULTY_1.setName("First faculty");
        FACULTY_1.setColor("black");
        FACULTY_2.setId(0L);
        FACULTY_2.setName("Second faculty");
        FACULTY_2.setColor("white");
        FACULTY_3.setId(0L);
        FACULTY_3.setName("Third faculty");
        FACULTY_3.setColor("scarlet");
        FACULTY_4.setId(0L);
        FACULTY_4.setName("Fourth faculty");
        FACULTY_4.setColor("gold");

        BING.setId(0L);
        BING.setName("Chendler Bing");
        BING.setAge(25);
        BING.setFaculty(GRIFFINDOR);

        JOE.setId(0L);
        JOE.setAge(24);
        JOE.setName("Joe Tribiany");
        JOE.setFaculty(GRIFFINDOR);

        ROSS.setId(0L);
        ROSS.setAge(25);
        ROSS.setName("Ross Geller");
        ROSS.setFaculty(COGTEVRAN);

        MONICA.setId(0L);
        MONICA.setAge(24);
        MONICA.setName("Monica Bing");
        MONICA.setFaculty(PUFFENDUY);

        RACHEL.setId(0L);
        RACHEL.setAge(27);
        RACHEL.setName("Rachel Grin");
        RACHEL.setFaculty(SLIZERIN);

        FUIBY.setId(0L);
        FUIBY.setAge(28);
        FUIBY.setName("Fuiby Buffey");
        FUIBY.setFaculty(COGTEVRAN);

    }
}
