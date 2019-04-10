package com.santos.generic.Activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.santos.firestoremeth.FirebaseMethods;
import com.santos.firestoremeth.Models.Cursos;
import com.santos.firestoremeth.Models.Notas;
import com.santos.generic.Fragmentos.NotasFragment;
import com.santos.generic.Fragmentos.TareasFragment;
import com.santos.generic.Interfaz.IMainMaestro;
import com.santos.generic.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import id.zelory.compressor.Compressor;
import yuku.ambilwarna.AmbilWarnaDialog;

import static com.santos.firestoremeth.Nodos.NODO_CURSOS;
import static com.santos.firestoremeth.Nodos.NODO_NOTAS;
import static com.santos.generic.Utils.palReb.KEY_NOTAS;

public class TabActivity extends AppCompatActivity implements IMainMaestro {

    private static final String TAG = "TabActivity";
    public static final String FOTO1 = "https://firebasestorage.googleapis.com/v0/b/trigonometria-1c5cb.appspot.com/o/Imagenes%2Fnoimage.png?alt=media&token=1e1df63e-25ef-42b0-8520-3cd0992799c3";
    private static final int GalleriaPick = 1;

    //Widgets
    private AppBarLayout appBar;
    private TabLayout pestanas;
    private ViewPager viewPager;
    private Dialog epicDialog;

    //FirebaseMethods
    private FirebaseMethods firebaseMethods;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore db;
    private StorageReference mStorageReference;

    private String url_imagen;
    public static String id_docuento;
    private int currentColor;
    private Cursos cursos = null;
    private Uri mImageUri;
    private Bitmap thumb_bitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

        epicDialog = new Dialog(this);
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        currentColor = ContextCompat.getColor(this, R.color.colorAccent);

        Intent i = getIntent();
        cursos = i.getParcelableExtra(KEY_NOTAS);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(cursos.getNombre_curso());

        toolbar.setNavigationOnClickListener(v -> finish());
        if (cursos != null) {

            id_docuento = cursos.getId_curso();

            //Donde se carggaran las tabs
            viewPager = findViewById(R.id.htab_viewpager);
            setupViewPager(viewPager);

            TabLayout tabLayout = findViewById(R.id.htab_tabs);
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
            tabLayout.setupWithViewPager(viewPager);

            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {

                    viewPager.setCurrentItem(tab.getPosition());
                    Log.d(TAG, "onTabSelected: pos: " + tab.getPosition());

                    switch (tab.getPosition()) {
                        case 0:
                            //Toast.makeText(GradoDetalleActivity.this, "Hola", Toast.LENGTH_SHORT).show();
                            break;
                        case 1:
                            //Toast.makeText(MangaDetalleActivity.this, "Hola No.2", Toast.LENGTH_SHORT).show();
                            break;
                        case 2:
                            //Toast.makeText(MangaDetalleActivity.this, "Hola No.3", Toast.LENGTH_SHORT).show();
                            break;
                        case 3:
                            //Toast.makeText(MangaDetalleActivity.this, "Hola No.3", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        //bundle
        /*Bundle bundle = new Bundle();
        bundle.putStringArrayList("DATE", materias);*/

        /*FormulasFragment tabTareasFragment = new FormulasFragment();

        tabTareasFragment.setArguments(bundle);*/
        //adapter.addFrag(tabTareasFragment, "Notas");
        adapter.addFrag(new NotasFragment(), "Notas");
        adapter.addFrag(new TareasFragment(), "Tareas");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onNotaSeleccionada(Notas notas) {

    }

    @Override
    public void onNotaUpdate(Notas notas) {

    }

    @Override
    public void onCursotoNotaa(Cursos cursos) {

    }

    @Override
    public void onNuevoCuestionario(String titulo, String content) {

    }

    private static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nueva_nota, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            /*Intent intent = new Intent(TabActivity.this, OptionsActivity.class);
            intent.putExtra(KEY_NOTAS, id_docuento);
            startActivity(intent);*/
            //showTheNewDialog(R.style.DialogScale);
            //mAdaptadorMaestrosCompleto.notifyDataSetChanged();
            //startActivity(new Intent(getApplicationContext(), NuevoCursooActivity.class));
            //showTheNewDialog(R.style.DialogScale);
            //mAdaptadorMaestrosCompleto.notifyDataSetChanged();
            /*return true;
        } else if (id == R.id.action_color) {
            openDialog(true);
        } else if (id == R.id.action_delete_curso) {
            getDeleteGrado();
        }
        return super.onOptionsItemSelected(item);
    }*/

    private void getDeleteGrado() {
        AlertDialog.Builder builder = new AlertDialog.Builder(TabActivity.this);

        builder.setTitle("Confirmar");
        builder.setMessage("Esta seguro de eliminar este curso?");

        builder.setPositiveButton("SI", (dialog, which) -> {
            // Do nothing but close the dialog
            db = FirebaseFirestore.getInstance();

            //deleteCuestionario();

            DocumentReference noteRef = db
                    .collection(NODO_CURSOS)
                    .document(cursos.getId_curso());

            noteRef.delete().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(TabActivity.this, "Curso Eliminado", Toast.LENGTH_SHORT).show();
                    //mNoteRecyclerViewAdapter.removeNote(note);
                } else {
                    Toast.makeText(TabActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            });
            dialog.dismiss();
            TabActivity.this.finish();
        });

        builder.setNegativeButton("NO", (dialog, which) -> {

            // Do nothing
            dialog.dismiss();
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void deleteNotas() {
        AlertDialog.Builder builder = new AlertDialog.Builder(TabActivity.this);

        builder.setTitle("Confirmar");
        builder.setMessage("Esta seguro de eliminar este curso?");

        builder.setPositiveButton("SI", (dialog, which) -> {
            // Do nothing but close the dialog
            db = FirebaseFirestore.getInstance();

            DocumentReference noteRef = db
                    .collection(NODO_CURSOS)
                    .document(cursos.getId_curso());

            noteRef.delete().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(TabActivity.this, "Curso Eliminado", Toast.LENGTH_SHORT).show();
                    //mNoteRecyclerViewAdapter.removeNote(note);
                } else {
                    Toast.makeText(TabActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            });
            dialog.dismiss();
            TabActivity.this.finish();
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Do nothing
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void deleteArchivos() {
        AlertDialog.Builder builder = new AlertDialog.Builder(TabActivity.this);

        builder.setTitle("Confirmar");
        builder.setMessage("Esta seguro de eliminar este curso?");

        builder.setPositiveButton("SI", (dialog, which) -> {
            // Do nothing but close the dialog
            db = FirebaseFirestore.getInstance();

            DocumentReference noteRef = db
                    .collection(NODO_CURSOS)
                    .document(cursos.getId_curso());

            noteRef.delete().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(TabActivity.this, "Curso Eliminado", Toast.LENGTH_SHORT).show();
                    //mNoteRecyclerViewAdapter.removeNote(note);
                } else {
                    Toast.makeText(TabActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            });
            dialog.dismiss();
            TabActivity.this.finish();
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Do nothing
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void deleteCuestionario() {
        db = FirebaseFirestore.getInstance();

        DocumentReference noteRef = db
                .collection(NODO_CURSOS)
                .document(cursos.getId_curso())
                .collection(NODO_NOTAS)
                .document();

        noteRef.delete().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(TabActivity.this, "Curso Eliminado", Toast.LENGTH_SHORT).show();
                //mNoteRecyclerViewAdapter.removeNote(note);
            } else {
                Toast.makeText(TabActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
        TabActivity.this.finish();
    }

    private void openDialog(boolean supportsAlpha) {
        AmbilWarnaDialog dialog = new AmbilWarnaDialog(this, currentColor, supportsAlpha, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                currentColor = color;
                //appBar.setBackgroundColor(color);
            }

            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
                Toast.makeText(getApplicationContext(), "Action canceled!", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }

    private void showTheNewDialog(int type) {

        //Inicializacion de nuestros metodos
        /*firebaseMethods = new FirebaseMethods(this, NODO_CURSOS, id_docuento, NODO_NOTAS);
        epicDialog.setContentView(R.layout.popup_alumnos);
        epicDialog.getWindow().getAttributes().windowAnimations = type;
        epicDialog.setCancelable(false);

        //Widgets
        final ImageView closePopupPositiveImg = epicDialog.findViewById(R.id.closePopupPositive);
        final Button aceptar = epicDialog.findViewById(R.id.btn_acept);
        final Button mButtonFoto = epicDialog.findViewById(R.id.btn_foto);
        final EditText mEditTextTitulo = epicDialog.findViewById(R.id.note_title);
        final EditText mEditTextApellidos = epicDialog.findViewById(R.id.tidt_apellido);
        final EditText mEditTextEdad = epicDialog.findViewById(R.id.tiet_edad);

        closePopupPositiveImg.setOnClickListener(v -> epicDialog.dismiss());

        mStorageReference = FirebaseStorage.getInstance().getReference("Imagenes");

        mButtonFoto.setOnClickListener(v -> {
            Intent galeriaIntent = new Intent();
            galeriaIntent.setAction(Intent.ACTION_GET_CONTENT);
            galeriaIntent.setType("image/*");
            startActivityForResult(galeriaIntent, GalleriaPick);
        });

        aceptar.setOnClickListener(v -> {
            //Todo: obtenemos los valores de las vistas corresponidnetes
            String nombre = mEditTextTitulo.getText().toString();
            String apellidos = mEditTextApellidos.getText().toString();
            String edad = mEditTextEdad.getText().toString();

            if (checkInputs(nombre, apellidos, edad)) {
                if (url_imagen == null)
                    url_imagen = FOTO1;

                crearNuevoAlumno(nombre, apellidos, edad);
                epicDialog.dismiss();
            }
        });

        epicDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        epicDialog.show();*/
    }

    private boolean checkInputs(String nombres, String apellidos, String edad) {
        if (nombres.equals("") || apellidos.equals("") || edad.equals("")) {
            Toast.makeText(this, "Todos los compos son obligatorios", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void crearNuevoAlumno(String nombre, String apellidos, String edad) {
        //firebaseMethods.registrarNuevoEmail(correo,"123456789");


        firebaseMethods.nuevaNota(
                nombre,
                apellidos,
                edad,
                firebaseUser.getDisplayName(),
                firebaseUser.getPhotoUrl().toString(),
                firebaseUser.getEmail(),
                url_imagen,
                firebaseUser.getUid());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        final StorageReference fileReference = mStorageReference.child(firebaseUser.getUid() + getDate() + ".jpg");

        if (requestCode == GalleriaPick && resultCode == RESULT_OK && data != null && data.getData() != null) {

            // start picker to get image for cropping and then use the image in cropping activity
            CropImage.activity(data.getData())
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);


            /*mImageUri = data.getData();

            File thumb_file_path = new File(mImageUri.getPath());
            thumb_file_path.getAbsolutePath();

            Bitmap thumb_bitmap = null;
            try {
                thumb_bitmap = new Compressor(this)
                        .setMaxWidth(200)
                        .setMaxHeight(200)
                        .setQuality(75)
                        .compressToBitmap(thumb_file_path);
            } catch (IOException e) {
                e.printStackTrace();
            }

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 2, byteArrayOutputStream);
            final byte[] thumb_byte = byteArrayOutputStream.toByteArray();

            UploadTask uploadTask = fileReference.putBytes(thumb_byte);
            uploadTask.addOnCompleteListener(task -> {
                String url = fileReference.getDownloadUrl().toString();
                Toast.makeText(this, url, Toast.LENGTH_SHORT).show();
            });*/
//---------------------------------------------------------------------------------------------------------------------
            /*Task<Uri> urlTask = fileReference.putFile(mImageUri).continueWithTask(task -> {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return fileReference.getDownloadUrl();
            }).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    url_imagen = downloadUri.toString();
                } else {
                    // Handle failures
                    // ...
                }
            });*/

            /*if (urlTask != null) {
                Toast.makeText(this, "Subido", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            }*/
          /*  fileReference.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    url_imagen = StorageReference.getDownloadUrl().toString();
                    Toast.makeText(TabActivity.this, "Foto Subida", Toast.LENGTH_SHORT).show();
                }
            }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                    System.out.println("Upload is paused");
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    System.out.println("Upload is " + progress + "% done");
                }
            });*/
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                mImageUri = result.getUri();
                File thumb_file_path = new File(mImageUri.getPath());
                thumb_file_path.getAbsolutePath();

                try {
                    thumb_bitmap = new Compressor(this)
                            .setQuality(75)
                            .compressToBitmap(thumb_file_path);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 75, byteArrayOutputStream);
                final byte[] thumb_byte = byteArrayOutputStream.toByteArray();

                Task<Uri> uriTask = fileReference.putBytes(thumb_byte).continueWithTask(task -> {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return fileReference.getDownloadUrl();
                }).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        url_imagen = downloadUri.toString();
                    } else {
                        // Handle failures
                        // ...
                    }
                });

                if (uriTask != null) {
                    Toast.makeText(this, "Subido", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                }
                /*UploadTask uploadTask = fileReference.putBytes(thumb_byte);

                uploadTask.addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        url_imagen = fileReference.getDownloadUrl().toString();
                    }
                    url_imagen = fileReference.getDownloadUrl().toString();

                    /*String url = fileReference.getDownloadUrl().toString();
                    UploadTask.TaskSnapshot downloadUri = task.getResult();
                    url_imagen = downloadUri.toString();*/
                   /* Toast.makeText(this, url_imagen, Toast.LENGTH_SHORT).show();
                });*/
            }

        }
    }

    private String getDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE dd MMMM yyyy  HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

}
