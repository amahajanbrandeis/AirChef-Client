package edu.cs.brandeis.marius.homecooked;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import android.content.*;
import android.net.*;
import android.graphics.*;
import android.provider.*;

import org.json.JSONObject;

public class EditProfile extends AppCompatActivity {
    private int PICK_IMAGE_REQUEST = 321;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        final Button pickImageButton = (Button) findViewById(R.id.select_picture);
        final Button finishEditProfile = (Button) findViewById(R.id.finish_button);

        pickImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });

        finishEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                final EditText locationField = (EditText) findViewById(R.id.location_field);
                final EditText bioField = (EditText) findViewById(R.id.bio_field);
                final EditText eatField = (EditText) findViewById(R.id.eat_field);
                final EditText cookField = (EditText) findViewById(R.id.cook_field);
                final ImageView profilePic = (ImageView) findViewById(R.id.image_display);
                String location = locationField.getText().toString();
                String bio = bioField.getText().toString();
                String eat = eatField.getText().toString();
                String cook = cookField.getText().toString();
                Drawable image = profilePic.getDrawable();
                JSONObject profileInfo = new JSONObject();
                try {
                    profileInfo.put("location", location);
                    profileInfo.put("bio", bio);
                    profileInfo.put("eat", eat);
                    profileInfo.put("cook", cook);
                    profileInfo.put("profile_picture", image);
                }catch(Exception e){
                    ;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null){
            Uri imageUri = data.getData();

            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                ImageView imageView = (ImageView) findViewById(R.id.image_display);
                imageView.setImageBitmap(bitmap);
            }catch(Exception e){
                ;
            }
        }
    }
}
