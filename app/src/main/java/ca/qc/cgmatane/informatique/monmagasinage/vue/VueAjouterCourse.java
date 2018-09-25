package ca.qc.cgmatane.informatique.monmagasinage.vue;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

import ca.qc.cgmatane.informatique.monmagasinage.R;
import ca.qc.cgmatane.informatique.monmagasinage.donnees.CourseDAO;
import ca.qc.cgmatane.informatique.monmagasinage.modele.Course;

public class VueAjouterCourse extends AppCompatActivity {

    private final int AJOUTER_PRODUIT_ID_RETOUR=0;

    private CourseDAO courseDAO = CourseDAO.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vue_ajouter_course);

        Button actionNaviguerAjouterProduitCourse = (Button) findViewById(R.id.vue_ajouter_course_action_ajouter_produit);

        final Course course = new Course();

        actionNaviguerAjouterProduitCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentionNaviguerAjouterProduitCourse = new Intent(VueAjouterCourse.this, VueAjouterProduitListeCourse.class);
                ArrayList<Integer> produits = new ArrayList<>();
                produits.add(1);
                produits.add(3);
                produits.add(4);
                intentionNaviguerAjouterProduitCourse.putExtra("course", produits);
                startActivityForResult(intentionNaviguerAjouterProduitCourse, AJOUTER_PRODUIT_ID_RETOUR);
            }
        });

        Button actionNaviguerEnregistrerCourse = (Button) findViewById(R.id.vue_ajouter_course_action_enregistrer);

        actionNaviguerEnregistrerCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
