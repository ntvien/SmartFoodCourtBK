package com.example.smartfoodcourt;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartfoodcourt.Common.Common;
import com.example.smartfoodcourt.Database.Database;
import com.example.smartfoodcourt.Model.CartItem;
import com.example.smartfoodcourt.Model.Comment;
import com.example.smartfoodcourt.Model.Food;
import com.example.smartfoodcourt.ui.food.FoodViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class FoodDetail extends AppCompatActivity {
    TextView txtName, txtPrice, txtDes, txtDiscount;
    ImageView imgFood;
    String foodID;
    DatabaseReference foodList;

    Button btnBackDetail;
    ImageView imgAddCart;
    ImageView btnUp, btnDown, imgDiscount;
    TextView txtQuantity;
    Food food;


    private FoodViewModel foodViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        txtName = findViewById(R.id.txtName);
        txtPrice = findViewById(R.id.txtPrice);
        txtDes = findViewById(R.id.txtDes);
        txtDiscount = findViewById(R.id.txtDiscount);
        imgFood = findViewById(R.id.imgFood);

        imgDiscount = findViewById(R.id.imgDiscount);

        btnBackDetail = (Button)findViewById(R.id.btnBackDetail);

        imgAddCart = (ImageView) findViewById(R.id.imgAddCart);
        btnDown = findViewById(R.id.imgDown);
        btnUp = findViewById(R.id.imgUp);
        txtQuantity = findViewById(R.id.txtQuantity);

        foodList = FirebaseDatabase.getInstance().getReference("Food");

        if(getIntent() != null) {
            foodID = getIntent().getStringExtra("foodID");
            if(!foodID.isEmpty()) loadFood();
        }

        btnBackDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer a = (Integer.parseInt(txtQuantity.getText().toString()) + 1);
                txtQuantity.setText(a.toString());
            }
        });

        btnDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Integer a = (Integer.parseInt(txtQuantity.getText().toString()) - 1);
                if(a > 0){
                txtQuantity.setText(a.toString());
                }
            }
        });



        FloatingActionButton btnStar = findViewById(R.id.btnStar);
        btnStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogRating();
            }
        });

        FloatingActionButton btnComment = findViewById(R.id.btnComment);
        btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cartIntent = new Intent(FoodDetail.this, Cart.class);
                startActivity(cartIntent);
            }
        });

        imgAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Database(getBaseContext()).addToCart(new CartItem(
                        foodID,
                        food.getName(),
                        food.getPrice(),
                        txtQuantity.getText().toString(),
                        food.getDiscount()
                ));

                Toast.makeText(FoodDetail.this, "Added to my cart", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void showDialogRating() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getBaseContext());

        builder.setTitle("Rating Food");
        builder.setMessage("Please fill information");

        View itemView = LayoutInflater.from(getBaseContext()).inflate(R.layout.rating_layout, null);

        final RatingBar ratingBar = (RatingBar)itemView.findViewById(R.id.ratingBar);
        final EditText edtComment = (EditText)itemView.findViewById(R.id.edtComment);

        builder.setView(itemView);
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Comment comment = new Comment();
                comment.setName(Common.currentUser.getPhone());
                comment.getId(Common.currentUser.getEmail());
                comment.setComment(edtComment.getText().toString());
                comment.setRatingValue(ratingBar.getRating());
                Map<String, Object> serverTimeStamp = new HashMap<>();
                serverTimeStamp.put("timeStamp", ServerValue.TIMESTAMP);
                comment.setCommentTimeStamp(serverTimeStamp);


            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

//    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
//        foodViewModel = ViewModelProviders.of(this).get(FoodViewModel.class);
//        View root = inflater.inflate(R.layout.fragment_food, container,false);
//        unbider = ButterKnife.bind(this, root);
//        foodViewModel.getMutableLiveDataComment().observe(this, food);
//        displayInfo(food);
//        )};
//    }

    private void loadFood() {
        foodList.child(foodID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                   food = snapshot.getValue(Food.class);

                   Locale locale = new Locale("vi", "VN");
                   NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

                   Picasso.with(getBaseContext()).load(food.getImage()).into(imgFood);
                   txtPrice.setText(fmt.format(Integer.parseInt(food.getPrice())));
                   txtName.setText(food.getName());
                   txtDes.setText(food.getDescription());
                   txtDiscount.setText(food.getDiscount() + "%");

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
