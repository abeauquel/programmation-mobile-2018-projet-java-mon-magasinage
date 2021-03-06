package ca.qc.cgmatane.informatique.monmagasinage.donnees;

import android.content.ContentValues;
import android.database.Cursor;
import ca.qc.cgmatane.informatique.monmagasinage.donnees.base.BaseDeDonnees;
import ca.qc.cgmatane.informatique.monmagasinage.donnees.base.CourseSQL;
import ca.qc.cgmatane.informatique.monmagasinage.modele.Course;
import ca.qc.cgmatane.informatique.monmagasinage.modele.Magasin;
import ca.qc.cgmatane.informatique.monmagasinage.modele.pluriel.Courses;
import ca.qc.cgmatane.informatique.monmagasinage.modele.pluriel.LignesCourse;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CourseDAO implements CourseSQL{
    private MagasinDAO magasinDAO;
    private LigneCourseDAO ligneCourseDAO;
    private static CourseDAO instance = null;
    private BaseDeDonnees accesseurBaseDeDonnees;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm");

    protected Courses listeCourses;

    public static CourseDAO getInstance(){
        if(instance == null){
            instance = new CourseDAO();
        }
        return instance;
    }

    private CourseDAO() {
        this.listeCourses = new Courses();
        this.accesseurBaseDeDonnees = BaseDeDonnees.getInstance();
        this.magasinDAO = MagasinDAO.getInstance();
        this.ligneCourseDAO = LigneCourseDAO.getInstance();

        magasinDAO.listerMagasins();//Chargement des magasins
    }

    public Courses listerCoursesActuelles(){
        Cursor curseurCourses = accesseurBaseDeDonnees.getReadableDatabase().rawQuery(LISTER_COURSE_ACTUELLES,null );
        this.listeCourses.clear();


        Course course;

        int indexId = curseurCourses.getColumnIndex(Course.CHAMP_ID_COURSE);
        int indexNom = curseurCourses.getColumnIndex(Course.CHAMP_NOM);
        int indexDateNotification = curseurCourses.getColumnIndex(Course.CHAMP_DATE_NOTIFICATION);
        int indexDateRealisation = curseurCourses.getColumnIndex(Course.CHAMP_DATE_REALISATION);
        int indexIdMagasin = curseurCourses.getColumnIndex(Course.CHAMP_ID_MAGASIN);
        //On ne gere pas l'historique des courses ici
        int indexIdCourseOriginal = curseurCourses.getColumnIndex(Course.CHAMP_ID_COURSE_ORIGINAL);

        for(curseurCourses.moveToFirst();!curseurCourses.isAfterLast();curseurCourses.moveToNext()){
            int id_course= curseurCourses.getInt(indexId);
            String nom= curseurCourses.getString(indexNom);
            String str_dateNotification = curseurCourses.getString(indexDateNotification);
            String str_dateRealisation = curseurCourses.getString(indexDateRealisation);
            int id_magasin = curseurCourses.getInt(indexIdMagasin);
            int id_course_original = curseurCourses.getInt(indexIdCourseOriginal);

            LocalDateTime dateNotification;
            LocalDateTime dateRealisation;
            if(null != str_dateNotification && !str_dateNotification.equals("")){
                dateNotification = LocalDateTime.parse(str_dateNotification, formatter);
            }else {
                dateNotification =null;
            }
            if(null != str_dateRealisation && !str_dateRealisation.equals("")){
                dateRealisation = LocalDateTime.parse(str_dateRealisation, formatter);
            }else {
                dateRealisation = null;
            }

            course = new Course(id_course, nom, dateNotification, dateRealisation);
            course.setMonMagasin(magasinDAO.getListeMagasins().trouverAvecId(id_magasin));

            course.setCourseOriginal(new Course(id_course_original)); // Ajout d'une course fictive uniquement pour sauvegarder l'id
            this.listeCourses.add(course);
        }

        curseurCourses.close();

        return this.listeCourses;
    }

    public Courses listerToutesLesCourses(){
        Cursor curseurCourses = accesseurBaseDeDonnees.getReadableDatabase().rawQuery(LISTER_COURSE, null);
        //this.listeCourses.clear();

        Course course;
        Courses listeDeToutesLesCourses= new Courses();
        int indexId = curseurCourses.getColumnIndex(Course.CHAMP_ID_COURSE);
        int indexNom = curseurCourses.getColumnIndex(Course.CHAMP_NOM);
        int indexDateNotification = curseurCourses.getColumnIndex(Course.CHAMP_DATE_NOTIFICATION);
        int indexDateRealisation = curseurCourses.getColumnIndex(Course.CHAMP_DATE_REALISATION);
        int indexIdMagasin = curseurCourses.getColumnIndex(Course.CHAMP_ID_MAGASIN);

        int indexIdCourseOriginal = curseurCourses.getColumnIndex(Course.CHAMP_ID_COURSE_ORIGINAL);

        for(curseurCourses.moveToFirst();!curseurCourses.isAfterLast();curseurCourses.moveToNext()){
            int id_course= curseurCourses.getInt(indexId);
            String nom= curseurCourses.getString(indexNom);
            String str_dateNotification = curseurCourses.getString(indexDateNotification);
            String str_dateRealisation = curseurCourses.getString(indexDateRealisation);
            int id_magasin = curseurCourses.getInt(indexIdMagasin);
            int id_course_original = curseurCourses.getInt(indexIdCourseOriginal);

            LocalDateTime dateNotification;
            LocalDateTime dateRealisation;
            if(null != str_dateNotification && !str_dateNotification.equals("")){
                dateNotification = LocalDateTime.parse(str_dateNotification, formatter);
            }else {
                dateNotification =null;
            }
            if(null != str_dateRealisation && !str_dateRealisation.equals("")){
                dateRealisation = LocalDateTime.parse(str_dateRealisation, formatter);
            }else {
                dateRealisation = null;
            }

            course = new Course(id_course, nom, dateNotification, dateRealisation);
            course.setMonMagasin(magasinDAO.getListeMagasins().trouverAvecId(id_magasin));
            course.setCourseOriginal(new Course(id_course_original)); // Ajout d'une course fictive uniquement pour sauvegarder l'id
            listeDeToutesLesCourses.add(course);
        }

        curseurCourses.close();

        return listeDeToutesLesCourses;
    }

    public Courses listerHistoriqueDuneCourse(Course maCourse){
        Cursor curseurCourses = accesseurBaseDeDonnees.getReadableDatabase().rawQuery(LISTER_HISTORIQUE_DUNE_COURSE, new String [] {String.valueOf(maCourse.getCourseOriginal().getId()),String.valueOf(maCourse.getCourseOriginal().getId()) });

        Course course;
        Courses listeHistoriqueCourse= new Courses();
        int indexId = curseurCourses.getColumnIndex(Course.CHAMP_ID_COURSE);
        int indexNom = curseurCourses.getColumnIndex(Course.CHAMP_NOM);
        int indexDateNotification = curseurCourses.getColumnIndex(Course.CHAMP_DATE_NOTIFICATION);
        int indexDateRealisation = curseurCourses.getColumnIndex(Course.CHAMP_DATE_REALISATION);
        int indexIdMagasin = curseurCourses.getColumnIndex(Course.CHAMP_ID_MAGASIN);
        int indexIdCourseOriginal = curseurCourses.getColumnIndex(Course.CHAMP_ID_COURSE_ORIGINAL);

        for(curseurCourses.moveToFirst();!curseurCourses.isAfterLast();curseurCourses.moveToNext()){
            int id_course= curseurCourses.getInt(indexId);
            String nom= curseurCourses.getString(indexNom);
            String str_dateNotification = curseurCourses.getString(indexDateNotification);
            String str_dateRealisation = curseurCourses.getString(indexDateRealisation);
            int id_magasin = curseurCourses.getInt(indexIdMagasin);
            int id_course_original = curseurCourses.getInt(indexIdCourseOriginal);

            LocalDateTime dateNotification;
            LocalDateTime dateRealisation;
            if(null != str_dateNotification && !str_dateNotification.equals("")){
                dateNotification = LocalDateTime.parse(str_dateNotification, formatter);
            }else {
                dateNotification =null;
            }
            if(null != str_dateRealisation && !str_dateRealisation.equals("")){
                dateRealisation = LocalDateTime.parse(str_dateRealisation, formatter);
            }else {
                dateRealisation = null;
            }

            course = new Course(id_course, nom, dateNotification, dateRealisation);
            course.setMonMagasin(magasinDAO.getListeMagasins().trouverAvecId(id_magasin));

            course.setCourseOriginal(new Course(id_course_original)); // Ajout d'une course fictive uniquement pour sauvegarder l'id
            if(course.getId() != maCourse.getId())
                listeHistoriqueCourse.add(course);
        }

        curseurCourses.close();

        return listeHistoriqueCourse;
    }

    public int creerCourse(String nom, String dateNotification, String dateRealisation, int idOriginal, Magasin magasin, LignesCourse ligneCourses)
    {

        ContentValues values = new ContentValues();
        values.put(Course.CHAMP_NOM,nom);
        values.put(Course.CHAMP_DATE_NOTIFICATION,dateNotification);
        values.put(Course.CHAMP_DATE_REALISATION,dateRealisation);
        values.put(Course.CHAMP_ID_COURSE_ORIGINAL,idOriginal);
        values.put(Course.CHAMP_ID_MAGASIN, magasin.getId());


        int newId = (int) accesseurBaseDeDonnees.getWritableDatabase().insert(Course.NOM_TABLE,null, values);

        LocalDateTime dateNotificationFormatted = null;
        LocalDateTime dateRealisationFormatted = null;

        if (dateNotification != null && !"".equals(dateNotification))
            dateNotificationFormatted = LocalDateTime.parse(dateNotification, formatter);

        if (dateRealisation != null && !"".equals(dateRealisation))
            dateRealisationFormatted = LocalDateTime.parse(dateRealisation, formatter);

        Course course = new Course(newId, nom, dateNotificationFormatted, dateRealisationFormatted);
        course.setMonMagasin(magasin);
        course.setCourseOriginal(new Course(idOriginal));
        course.setMesLignesCourse(ligneCourses);
        this.listeCourses.add(course);
        ligneCourses.setCourseDeToutesLesLignes(course);
        ligneCourseDAO.enregistrerListeLigneCoursePourUneCourse(course.getId(), ligneCourses);

        return course.getId();
    }


    public void modifierCourse(int id, String nom, String dateNotification, String dateRealisation, int idOriginal, Magasin magasin)
    {

        ContentValues values = new ContentValues();
        values.put(Course.CHAMP_NOM,nom);
        values.put(Course.CHAMP_DATE_NOTIFICATION,dateNotification);
        values.put(Course.CHAMP_DATE_REALISATION,dateRealisation);
        values.put(Course.CHAMP_ID_MAGASIN, magasin.getId());

        LocalDateTime dateNotificationFormatted = null;
        LocalDateTime dateRealisationFormatted = null;

        accesseurBaseDeDonnees.getWritableDatabase().update(Course.NOM_TABLE,
                values,
                Course.CHAMP_ID_COURSE+"="+id,
                null);

        if (dateNotification != null && !"".equals(dateNotification))
            dateNotificationFormatted = LocalDateTime.parse(dateNotification, formatter);

        if (dateRealisation != null && !"".equals(dateRealisation))
            dateRealisationFormatted = LocalDateTime.parse(dateRealisation, formatter);

        Course courseAmodifier = this.getListeCourses().trouverAvecId(id);
        if(ligneCourseDAO.enregistrerListeLigneCoursePourUneCourse(courseAmodifier.getId(), courseAmodifier.getMesLignesCourse())){
            courseAmodifier.setNom(nom);
            courseAmodifier.setDateNotification(dateNotificationFormatted);
            courseAmodifier.setDateRealisation(dateRealisationFormatted);
            courseAmodifier.setMonMagasin(magasin);
            courseAmodifier.setCourseOriginal(new Course(idOriginal));
        }

    }

    /***
     * Cloture une course ajoutant une date de réalisation et en créant une nouvelle course lié à cette derniere
     * @param courseACloturer
     */
    public void cloturerCourse(Course courseACloturer){

        ContentValues values = new ContentValues();
        values.put(Course.CHAMP_DATE_REALISATION, LocalDateTime.now().format(formatter));
        accesseurBaseDeDonnees.getWritableDatabase().update(Course.NOM_TABLE, values, Course.CHAMP_ID_COURSE+"="+courseACloturer.getId(), null);

        int id_course_original = courseACloturer.getId();
        if(courseACloturer.getCourseOriginal().getId() != 0)
            id_course_original=courseACloturer.getCourseOriginal().getId();

        creerCourse(courseACloturer.getNom(),
                "",
                "" ,
                id_course_original,
                courseACloturer.getMonMagasin(),
                courseACloturer.getMesLignesCourse());

        listeCourses.remove(courseACloturer);
    }

    /***
     * Renvoi une course en fonction de son id
     * @return
     */
    public Course trouverParId(int idCourse){
        Cursor curseurCourses = accesseurBaseDeDonnees.getReadableDatabase().rawQuery(TROUVER_PAR_ID, new String[]{String.valueOf(idCourse)});

        Course course =null;
        int indexId = curseurCourses.getColumnIndex(Course.CHAMP_ID_COURSE);
        int indexNom = curseurCourses.getColumnIndex(Course.CHAMP_NOM);
        int indexDateNotification = curseurCourses.getColumnIndex(Course.CHAMP_DATE_NOTIFICATION);
        int indexDateRealisation = curseurCourses.getColumnIndex(Course.CHAMP_DATE_REALISATION);
        int indexIdMagasin = curseurCourses.getColumnIndex(Course.CHAMP_ID_MAGASIN);
        int indexIdCourseOriginal = curseurCourses.getColumnIndex(Course.CHAMP_ID_COURSE_ORIGINAL);

        if(curseurCourses.getCount() > 0) {
            curseurCourses.moveToNext();

            int id_course = curseurCourses.getInt(indexId);
            String nom = curseurCourses.getString(indexNom);
            String str_dateNotification = curseurCourses.getString(indexDateNotification);
            String str_dateRealisation = curseurCourses.getString(indexDateRealisation);
            int id_magasin = curseurCourses.getInt(indexIdMagasin);
            int id_course_original = curseurCourses.getInt(indexIdCourseOriginal);

            LocalDateTime dateNotification;
            LocalDateTime dateRealisation;
            if (null != str_dateNotification && !str_dateNotification.equals("")) {
                dateNotification = LocalDateTime.parse(str_dateNotification, formatter);
            } else {
                dateNotification = null;
            }
            if (null != str_dateRealisation && !str_dateRealisation.equals("")) {
                dateRealisation = LocalDateTime.parse(str_dateRealisation, formatter);
            } else {
                dateRealisation = null;
            }

            course = new Course(id_course, nom, dateNotification, dateRealisation);
            course.setMonMagasin(magasinDAO.getListeMagasins().trouverAvecId(id_magasin));
            course.setCourseOriginal(new Course(id_course_original)); // Ajout d'une course fictive uniquement pour sauvegarder l'id
        }
        curseurCourses.close();

        return course;
    }

    public Courses getListeCourses() {
        return listeCourses;
    }

    public void setListeCourses(Courses listeCourses) {
        this.listeCourses = listeCourses;
    }

    public void supprimerCourse(Course courseActuelle) {
        accesseurBaseDeDonnees.getWritableDatabase().delete(Course.NOM_TABLE,
                Course.CHAMP_ID_COURSE+"="+courseActuelle.getId(),
                null);
    }
}
