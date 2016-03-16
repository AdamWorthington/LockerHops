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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;
import java.util.ArrayList;
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
    private double totalCost = 0.0;
    public double getTotalCost()
    {
        return totalCost;
    }
    private void setTotalCost(double newValue)
    {
        double oldValue = totalCost;
        totalCost = newValue;
        notifyListeners("totalCost", oldValue, newValue);
    }

    private List<PropertyChangeListener> listener = new ArrayList<PropertyChangeListener>();

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

            int internalPos = 0;
            for (OptionItem oi : thisOption.getOptions())
            {
                RadioButton radio_button = new RadioButton(rg.getContext());
                radio_button.setText(oi.getName());
                radio_button.setTag(R.id.option_item_position, position);
                radio_button.setTag(R.id.option_item_internal_position, internalPos);
                radio_button.setChecked(oi.getIsSelected());
                rg.addView(radio_button);

                internalPos++;
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
                CheckBox checkbox = (CheckBox) vv.findViewById(R.id.config_option_checkbox);
                checkbox.setChecked(oi.getIsSelected());
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

        RadioButton b = (RadioButton) group.findViewById(checkedId);
        int optionPos = (int)b.getTag(R.id.option_item_position);
        int optionInternalPos = (int)b.getTag(R.id.option_item_internal_position);
        FoodItemOption thisOption = values.get(optionPos);
        for(OptionItem oi : thisOption.getOptions())
        {
            if(oi.getIsSelected())
            {
                oi.setIsSelected(false);
                totalCost -= oi.getPrice();
            }
        }
        OptionItem thisOptionItem = thisOption.getOptions().get(optionInternalPos);
        thisOptionItem.setIsSelected(true);
        totalCost += thisOptionItem.getPrice();

        if(thisOption.getChoiceCount() == 0)
            thisOption.incrementChoiceCount();
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
        thisOptionItem.setIsSelected(!isChecked);

        if(isChecked)
        {
            thisOption.decrementChoiceCount();
            setTotalCost(totalCost - thisOptionItem.getPrice());
        }
        else
        {
            thisOption.incrementChoiceCount();
            setTotalCost(totalCost + thisOptionItem.getPrice());
        }

        checkBox.performClick();
    }

    /*
     * checks to make sure that all option items that need to have a selection do have a selection.
     * If a section is not filled that needs to be, it will return the position that needs to be filled.
     * Otherwise it returns -1.
     */
    public int validateForm()
    {
        int position = -1;
        for(int i = 0; i < values.size(); i++)
        {
            FoodItemOption option = values.get(i);
            //If we must choose a certain number of items for an option and this is not met,
            //quit immediately and notify caller.
            if(!option.getCanChoose() && option.getChoiceCount() != option.getChooseLimit() ||
                    option.getCanChoose() && option.getChoiceCount() < option.getChooseMinimum()) {
                position = i;
                break;
            }
        }

        return position;
    }

    private void notifyListeners(String property,double oldValue, double newValue) {
        for (PropertyChangeListener name : listener) {
            name.propertyChange(new PropertyChangeEvent(this, property, oldValue, newValue));
        }
    }

    public void addChangeListener(PropertyChangeListener newListener) {
        listener.add(newListener);
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    public List<FoodItemOption> getValues()
    {
        return values;
    }
}

