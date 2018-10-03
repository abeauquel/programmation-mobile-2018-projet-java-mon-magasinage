package ca.qc.cgmatane.informatique.monmagasinage;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import org.xmlpull.v1.XmlPullParserException;

import java.util.HashMap;

import ca.qc.cgmatane.informatique.monmagasinage.donnees.CourseDAO;
import ca.qc.cgmatane.informatique.monmagasinage.donnees.base.BaseDeDonnees;
import ca.qc.cgmatane.informatique.monmagasinage.modele.Course;
import ca.qc.cgmatane.informatique.monmagasinage.modele.enumeration.EnumerationTheme;
import ca.qc.cgmatane.informatique.monmagasinage.modele.pluriel.Courses;
import ca.qc.cgmatane.informatique.monmagasinage.vue.CarteMagasin;
import ca.qc.cgmatane.informatique.monmagasinage.vue.VueAjouterCourse;
import ca.qc.cgmatane.informatique.monmagasinage.vue.VueListeMagasin;
import ca.qc.cgmatane.informatique.monmagasinage.vue.VueModifierCourse;
import ca.qc.cgmatane.informatique.monmagasinage.vue.VueModifierTheme;

public class ListeCourse extends AppCompatActivity {

    private static final int ACTIVITE_RESULTAT_MODIFIER_COURSE = 1;
    private static final int ACTIVITE_RESULTAT_MODIFIER_THEME = 2;

    /** Données*/
    protected Courses listeCourse;
    protected CourseDAO courseDAO;

    /** Composants graphiques*/
    protected SwipeMenuListView vueListViewCourse;
    protected SearchView vueBarreRechercheCourse;

    protected String rechercheUtilisateur ="";
    protected Courses listeCourseAffichage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        EnumerationTheme.recupererThemeSelectionnee(getApplicationContext());
        this.setTheme(EnumerationTheme.getThemeSelectionne().getIdLienSansActionBar());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.vue_liste_course);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /** Instanciation des composants graphiques*/
        vueListViewCourse = findViewById(R.id.vue_liste_course_listeview);
        vueBarreRechercheCourse = findViewById(R.id.vue_liste_course_barre_recherche);

        /** Instanciation des données*/
        BaseDeDonnees.getInstance(this); // Initialiser l'intance avec une activity
        courseDAO = CourseDAO.getInstance();
//        listeCourse = simulerListeCourse();
        listeCourse = courseDAO.listerCourses();
        listeCourseAffichage = new Courses();

        /** Affichage*/
        actualisationAffichage();

        FloatingActionButton actionNaviguerAjouterCourse = (FloatingActionButton) findViewById(R.id.vue_liste_course_action_naviguer_ajouter_course);
        actionNaviguerAjouterCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                Intent intentionNaviguerAjouterCourse = new Intent(ListeCourse.this, VueAjouterCourse.class);
                startActivityForResult(intentionNaviguerAjouterCourse, ACTIVITE_RESULTAT_MODIFIER_COURSE);
            }
        });

        vueBarreRechercheCourse.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                rechercheUtilisateur = newText;
                actualisationAffichage();
                return true;
            }
        });

        vueListViewCourse.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View vue, int position, long id) {
//                inserer ici redirection vers vue todo courses
                Toast message = Toast.makeText(getApplicationContext(), //display toast message
                        "Redirection vers liste faireCourses", Toast.LENGTH_SHORT);
                message.show();
            }
        });
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem editItem = new SwipeMenuItem(
                        getApplicationContext());
                editItem.setBackground(new ColorDrawable(Color.LTGRAY));
                editItem.setWidth(170);
                editItem.setIcon(R.drawable.ic_edit);
                menu.addMenuItem(editItem);

                SwipeMenuItem historyItem = new SwipeMenuItem(
                        getApplicationContext());
                historyItem.setBackground(new ColorDrawable(Color.LTGRAY));
                historyItem.setWidth(170);
                historyItem.setIcon(R.drawable.ic_history);
                menu.addMenuItem(historyItem);
            }
        };

        vueListViewCourse.setMenuCreator(creator);

        vueListViewCourse.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:

                        @SuppressWarnings("unchecked")
//                        HashMap<String,String> course =(HashMap<String,String>) vueListeCourse.getItemAtPosition((int)position);
                        Course course = listeCourse.get(position);
                        Intent intentionNaviguerModifierCourse = new Intent(ListeCourse.this, VueModifierCourse.class);
                        intentionNaviguerModifierCourse.putExtra(Course.CHAMP_ID_COURSE, course.getId()+"");
                        startActivityForResult(intentionNaviguerModifierCourse, ACTIVITE_RESULTAT_MODIFIER_COURSE);
                        break;
                    case 1:
                        Toast message = Toast.makeText(getApplicationContext(), //display toast message
                                "Redirection vers historique de la course", Toast.LENGTH_SHORT);
                        message.show();
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_liste_course, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_gestion_magasin) {
            Intent intentionNaviguerVueGestionMagasin = new Intent(this, VueListeMagasin.class);
            startActivity(intentionNaviguerVueGestionMagasin);
            return true;
        }
        else if(id == R.id.action_changer_theme){
            Intent intentionNaviguerChangerTheme = new Intent(this, VueModifierTheme.class);
            startActivityForResult(intentionNaviguerChangerTheme, ACTIVITE_RESULTAT_MODIFIER_THEME);
            return true;
        }
        else if(id == R.id.action_carte_magasin){
            //TODO à modifier navigation de puis la vue princpal pour tester l'implementation
            Intent intentionNaviguerCarteMagasin = new Intent(this, CarteMagasin.class);
            startActivity(intentionNaviguerCarteMagasin);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void actualisationAffichage() {
        listeCourseAffichage.clear();
        for(Course course: listeCourse){
            if(course.getNom().toLowerCase().contains(rechercheUtilisateur.toLowerCase())){
                listeCourseAffichage.add(course);
            }
        }

        afficherToutesLesCourses();
    }

    private void afficherToutesLesCourses(){
        SimpleAdapter adapterListeCourses = new SimpleAdapter(this, listeCourseAffichage.recuperereListePourAdapteur(), android.R.layout.two_line_list_item,
                new String[]{Course.CHAMP_NOM, Course.CHAMP_DATE_NOTIFICATION},
                new int[]{ android.R.id.text1, android.R.id.text2});

        vueListViewCourse.setAdapter(adapterListeCourses);
    }
    protected void onActivityResult(int activite, int resultat, Intent donnees){
        switch (activite){
            case ACTIVITE_RESULTAT_MODIFIER_COURSE:
                actualisationAffichage();
                break;
            case ACTIVITE_RESULTAT_MODIFIER_THEME:
                recreate();
                break;
        }
    }
}
