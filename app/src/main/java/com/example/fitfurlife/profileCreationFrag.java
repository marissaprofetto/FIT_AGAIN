package com.example.fitfurlife;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class profileCreationFrag extends Fragment {

    private EditText dogNameEditText;
    private EditText dogAgeEditText;
    private EditText dogWeightEditText;
    private EditText dogRaceEditText;
    private Button nextButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.profile_creation_fragment, container, false);

        initializeUIComponent(view);

        nextButton.setOnClickListener(v -> onSaveClicked());

        return view;
    }

    private void initializeUIComponent(View view) {
        dogNameEditText = view.findViewById(R.id.editTextDogName);
        dogAgeEditText = view.findViewById(R.id.editTextDogAge);
        dogWeightEditText = view.findViewById(R.id.editTextDogWeight);
        dogRaceEditText = view.findViewById(R.id.editTextDogRace);
        nextButton = view.findViewById(R.id.nextButton);
    }

    private void onSaveClicked() {
        if (validInput()) {
            String dogName = dogNameEditText.getText().toString();
            int dogAge = Integer.parseInt(dogAgeEditText.getText().toString());
            float dogWeight = Float.parseFloat(dogWeightEditText.getText().toString());
            String dogRace = dogRaceEditText.getText().toString();

            dogProfile dogProfile = new dogProfile(0, dogName, dogAge, dogWeight, dogRace);

            // Save the dog profile to the database
            databaseHelper dbHelper = new databaseHelper(getActivity());
            long id = dbHelper.insertDogProfile(dogProfile);

            if (id != -1) {
                // Successfully inserted the profile
                Toast.makeText(getActivity(), "Profile saved with ID: " + id, Toast.LENGTH_SHORT).show();

                // Transition to another Fragment or activity as needed
                Fragment nextFragment = new profileSelectionFrag(); // Replace with your next fragment class
                FragmentManager fragmentManager = getParentFragmentManager(); // Get the FragmentManager for this transaction
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.replace(R.id.fragmentContainer, nextFragment);
                fragmentTransaction.commit(); // Commit the transaction

            } else {
                // Failed to insert the profile
                Toast.makeText(getActivity(), "Failed to save profile", Toast.LENGTH_SHORT).show();
            }
        } else {

        }
    }


    private boolean validInput() {
        String dogName = dogNameEditText.getText().toString().trim();
        String dogAge = dogAgeEditText.getText().toString().trim();
        String dogWeight = dogWeightEditText.getText().toString().trim();
        String dogRace = dogRaceEditText.getText().toString().trim();

        // Check if the dog's name contains only letters and if it is within the character limit
        if (!dogName.matches("[a-zA-Z ]+") || (dogName.length() > 28)) {
            dogNameEditText.setError("Invalid name: Only letters and spaces allowed, max 28 characters");
            return false;
        }

        // Check if the dog's age is a number and within the allowed range
        try {
            int age = Integer.parseInt(dogAge);
            if (age <= 0 || age > 30) {
                dogAgeEditText.setError("Invalid age: Enter a positive number, max 30");
                return false;
            }
        } catch (NumberFormatException e) {
            dogAgeEditText.setError("Invalid age: Not a number");
            return false;
        }

        // Check if the dog's weight is a number and within the allowed range
        try {
            float weight = Float.parseFloat(dogWeight);
            if (weight <= 0 || weight > 360) {
                dogWeightEditText.setError("Invalid weight: Enter a positive number, max 360");
                return false;
            }
        } catch (NumberFormatException e) {
            dogWeightEditText.setError("Invalid weight: Not a number");
            return false;
        }

        // Check if the dog's race contains only letters and if it is within the character limit
        if (!dogRace.matches("[a-zA-Z ]+") || (dogRace.length() > 28)) {
            dogRaceEditText.setError("Invalid breed: Only letters and spaces allowed, max 28 characters");
            return false;
        }

        return true;
    }
}
