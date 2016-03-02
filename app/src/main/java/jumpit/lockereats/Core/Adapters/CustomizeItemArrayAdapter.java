package jumpit.lockereats.Core.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.List;

import jumpit.lockereats.Model.FoodItemOption;
import jumpit.lockereats.Model.OptionItem;
import jumpit.lockereats.R;

/**
 * Created by cdwil on 12/17/2015.
 */
public class CustomizeItemArrayAdapter extends RecyclerView.Adapter<CustomizeItemViewHolder>
{
    private List<FoodItemOption> values;
    private int itemLayout;

    public CustomizeItemArrayAdapter(List<FoodItemOption> values, int itemLayout)
    {
        this.values = values;
        this.itemLayout = itemLayout;
    }

    @Override
    public CustomizeItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int resource)
    {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(itemLayout, viewGroup, false);
        return new CustomizeItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CustomizeItemViewHolder holder, int position)
    {
        FoodItemOption thisOption = values.get(position);

        boolean canChoose = thisOption.getCanChoose();
        int minChoice = thisOption.getChooseMinimum();
        int maxChoice = thisOption.getChooseLimit();
        int curChoice = thisOption.getChoiceCount();
        String choiceString = "";
        int choiceColor = Color.BLACK;
        if(canChoose)
        {
            if(maxChoice == 0)
                choiceString = "Can choose any";
            else
                choiceString = "Can choose " + String.valueOf(maxChoice - curChoice);

            choiceColor = Color.GREEN;
        }
        else
        {
            if(minChoice - curChoice == 0)
            {
                choiceString = String.valueOf(curChoice) + " items chosen";
                choiceColor = Color.GREEN;
            }
            else
            {
                choiceString = "Must choose " + String.valueOf(minChoice - curChoice);
                choiceColor = Color.RED;
            }
        }

        holder.ItemChooseIndicatorTextView.setText(choiceString);
        holder.ItemChooseIndicatorTextView.setTextColor(choiceColor);
        holder.ItemCategoryTextView.setText(thisOption.getLabel());

        LayoutInflater inflater = (LayoutInflater) holder.ItemCategoryTextView.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

         if(!thisOption.getCanChoose() && thisOption.getChooseLimit() == 1)
        {
            View v = inflater.inflate(R.layout.layout_item_config_option_radio, holder.ItemListOptions, true);
            RadioGroup rg =  (RadioGroup) v.findViewById(R.id.config_option_radiogroup);

            for (OptionItem oi : thisOption.getOptions())
            {
                    RadioButton radio_button = new RadioButton(rg.getContext());
                    radio_button.setText(oi.getName());
                    rg.addView(radio_button);
            }

            rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    OnCheckedChanged(group, checkedId, holder);
                }
            });
        }
        /* If the option has multiple choice options, then we set up as check boxes*/
        else
        {
            int internalPos = 0;
            for (OptionItem oi : thisOption.getOptions())
            {
                /* If the option has multiple choice options, then we set up as check boxes*/
                View vv = inflater.inflate(R.layout.layout_item_config_option_checked, holder.ItemListOptions, false);
                holder.ItemListOptions.addView(vv);
                RelativeLayout rl = (RelativeLayout) vv.findViewById(R.id.config_option_rl);
                TextView configOptionNameView = (TextView) vv.findViewById(R.id.config_option_name);
                configOptionNameView.setText(oi.getName());
                TextView configOptionPriceView = (TextView) vv.findViewById(R.id.config_option_price);
                NumberFormat formatter = NumberFormat.getCurrencyInstance();
                String moneyString = formatter.format(oi.getPrice());
                configOptionPriceView.setText(moneyString);
                rl.setTag(R.id.option_item_position, position);
                rl.setTag(R.id.option_item_internal_position, internalPos);
                rl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClicked(v, holder);
                    }
                });

                internalPos++;
            }
        }
    }

    private void OnCheckedChanged(RadioGroup group, int checkedId, CustomizeItemViewHolder holder)
    {
        holder.ItemChooseIndicatorTextView.setTextColor(Color.GREEN);
    }

    private void onItemClicked(View v, CustomizeItemViewHolder holder)
    {
        int optionPos = (int)v.getTag(R.id.option_item_position);
        int optionInternalPos = (int)v.getTag(R.id.option_item_internal_position);
        FoodItemOption thisOption = values.get(optionPos);
        OptionItem thisOptionItem = thisOption.getOptions().get(optionInternalPos);

        //If we've maxed the number of choice
        if(thisOption.getCanChoose() && thisOption.getChooseLimit() != 0 && thisOption.getChoiceCount() == thisOption.getChooseLimit())
            return;

        CheckBox checkBox = (CheckBox) v.findViewById(R.id.config_option_checkbox);
        boolean isChecked = checkBox.isChecked();
        thisOptionItem.setIsSelected(isChecked);

        if(isChecked)
        {
            thisOption.decrementChoiceCount();

        }
        else
        {
            thisOption.incrementChoiceCount();
        }

        checkBox.performClick();
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

}
