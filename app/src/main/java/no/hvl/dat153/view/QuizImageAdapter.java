package no.hvl.dat153.view;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import no.hvl.dat153.R;
import no.hvl.dat153.activities.DatabaseActivity;
import no.hvl.dat153.database.QuizImageDAO;
import no.hvl.dat153.model.QuizImage;
import no.hvl.dat153.utils.ActivityUtils;

public class QuizImageAdapter extends RecyclerView.Adapter<QuizImageAdapter.ViewHolder> {

    List<QuizImage> items;

    public QuizImageAdapter(List<QuizImage> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public QuizImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View quizItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.quiz_item, parent, false);
        return new ViewHolder(quizItem);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizImageAdapter.ViewHolder holder, int position) {
        System.out.println(items.size());
        items.forEach(System.out::println);
        final QuizImage quizImage = items.get(position);

        holder.imageView.setImageBitmap(quizImage.getImage());
        holder.textView.setText(quizImage.getName());
        holder.imageButton.setOnClickListener(view -> {
            // Creates a builder for an alert dialog.
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            // Sets up the builder.
            builder.setMessage("Are you sure you want to delete the item, \"" + quizImage.getName() + "\"?")
                    .setPositiveButton("Yes", (dialog, id) -> {
                        // Gets the position of the item (in case it has changed)
                        int pos = items.indexOf(quizImage);
                        // Removes the item from the database.
                        if (QuizImageDAO.get().removeQuizImage(quizImage.getName())) {
                            // Makes the item disappear from the view.
                            notifyItemRemoved(pos);
                        }
                        Toast.makeText(view.getContext(),"The item \"" + quizImage.getName() + "\" has been deleted.",
                                Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("No", (dialog, id) -> {
                        // Cancels the dialog.
                        dialog.cancel();
                    });

            // Creates the alert dialog.
            AlertDialog alert = builder.create();
            // Sets the title.
            alert.setTitle("Deletion prompt");
            // Shows the alert dialog.
            alert.show();
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView;
        public ImageButton imageButton;

        public ViewHolder(View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.item_image);
            this.textView = (TextView) itemView.findViewById(R.id.item_name);
            this.imageButton = (ImageButton) itemView.findViewById(R.id.item_delete);
        }
    }
}
