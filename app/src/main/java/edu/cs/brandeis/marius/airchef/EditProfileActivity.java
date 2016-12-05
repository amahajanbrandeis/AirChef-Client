package edu.cs.brandeis.marius.airchef;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import android.content.*;
import android.net.*;
import android.graphics.*;
import android.provider.*;
import android.database.Cursor;

import org.json.JSONObject;

import java.io.File;


import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
//import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;

public class EditProfileActivity extends AppCompatActivity {
    private final int PICK_IMAGE_REQUEST = 321;
    private final String BUCKET_NAME = "airchefphotos";
    private final String PUBLIC_KEY = "AKIAI5ILXPJ6FA5K46NQ";
    private final String PRIVATE_KEY = "Q0h/lrAUMNhe8CWoKu9V2tRix9S3I8sIwM7zzN/n";
    private final String FOLDER = "profilepictures";


    private File imageFile;

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
                Intent intent = new Intent(EditProfileActivity.this, myProfileActivity.class);
                final EditText firstnameField = (EditText) findViewById(R.id.first_field);
                final EditText lastnameField = (EditText) findViewById(R.id.last_field);
                final EditText locationField = (EditText) findViewById(R.id.location_field);
                final EditText bioField = (EditText) findViewById(R.id.bio_field);
                final EditText eatField = (EditText) findViewById(R.id.eat_field);
                final EditText cookField = (EditText) findViewById(R.id.cook_field);
                final ImageView profilePic = (ImageView) findViewById(R.id.image_display);
                String first = firstnameField.getText().toString();
                String last = lastnameField.getText().toString();
                String location = locationField.getText().toString();
                String bio = bioField.getText().toString();
                String eat = eatField.getText().toString();
                String cook = cookField.getText().toString();
                String profilepic = getPictureUrl(imageFile, first+last);

                if(first.equals("") || last.equals("")){
                    Toast.makeText(getApplicationContext(), "Please fill in the required fields", Toast.LENGTH_SHORT);
                    return;
                }

                JSONObject profileInfo = new JSONObject();
                try {
                    profileInfo.put("firstname", first);
                    profileInfo.put("lastname", last);
                    profileInfo.put("location", location);
                    profileInfo.put("bio", bio);
                    profileInfo.put("eat", eat);
                    profileInfo.put("cook", cook);
                    profileInfo.put("picture", profilepic);
                }catch(Exception e){
                    ;
                }
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null){
            Uri imageUri = data.getData();
            imageFile = new File(getRealPathFromURI(this, imageUri));
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                ImageView imageView = (ImageView) findViewById(R.id.image_display);
                imageView.setImageBitmap(bitmap);
            }catch(Exception e){
                ;
            }
        }
    }

    /**
     * Gets the string of the filepath from a particular uri
     * This method was written with the help of stackoverflow user Misa Peic.
     * @param c
     * @param uri
     * @return
     */
    public String getRealPathFromURI(Context c, Uri uri){
        Cursor cursor = null;
        try{
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = c.getContentResolver().query(uri, proj, null, null, null);
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(columnIndex);
        }finally{
            if(cursor != null)
                cursor.close();
        }
    }

    public String getPictureUrl(File f, String key){
        String url = "";
        AmazonS3 s3 = new AmazonS3Client(new BasicAWSCredentials(PUBLIC_KEY, PRIVATE_KEY));
        TransferUtility trans = new TransferUtility(s3, this);
        TransferObserver obs = trans.upload(BUCKET_NAME, key, f);
        return "airchefphotos.s3-website-us-east-1.amazonaws.com/" + FOLDER + key;
    }
}
