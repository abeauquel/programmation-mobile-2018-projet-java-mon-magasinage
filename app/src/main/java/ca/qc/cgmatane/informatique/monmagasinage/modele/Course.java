package ca.qc.cgmatane.informatique.monmagasinage.modele;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class Course {
    public static final String NOM_TABLE = "course";

    public static final String CHAMP_ID_COURSE = "id_course";
    public static final String CHAMP_NOM = "nom";
    public static final String CHAMP_DATE_NOTIFICATION = "notification";
    public static final String CHAMP_DATE_REALISATION = "realisation";
    public static final String CHAMP_ID_COURSE_ORIGINAL = "id_course_original";

    private int id;
    private String nom;
    private LocalDateTime dateNotification;
    private LocalDateTime dateRealisation;
    private Course courseOriginal;

    public Course(int id, String nom, LocalDateTime dateNotification, LocalDateTime dateRealisation) {
        this.id = id;
        this.nom = nom;
        this.dateNotification = dateNotification;
        this.dateRealisation = dateRealisation;
    }

    public Course() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public LocalDateTime getDateNotification() {
        return dateNotification;
    }

    public void setDateNotification(LocalDateTime dateNotification) {
        this.dateNotification = dateNotification;
    }

    public LocalDateTime getDateRealisation() {
        return dateRealisation;
    }

    public void setDateRealisation(LocalDateTime dateRealisation) {
        this.dateRealisation = dateRealisation;
    }

    public Course getCourseOriginal() {
        return courseOriginal;
    }

    public void setCourseOriginal(Course courseOriginal) {
        this.courseOriginal = courseOriginal;
    }

    public HashMap<String, String> obtenirObjetPourAdapteur() {
        HashMap<String, String> sportPourAdapteur = new HashMap<String, String>();
        sportPourAdapteur.put(CHAMP_ID_COURSE, String.valueOf(this.id));
        sportPourAdapteur.put(CHAMP_NOM, this.nom);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy  hh:mm");
        sportPourAdapteur.put(CHAMP_DATE_NOTIFICATION,"pour le : "+ this.dateNotification.format(formatter));
        sportPourAdapteur.put(CHAMP_DATE_REALISATION, this.dateRealisation.format(formatter));
        return sportPourAdapteur;
    }
}
