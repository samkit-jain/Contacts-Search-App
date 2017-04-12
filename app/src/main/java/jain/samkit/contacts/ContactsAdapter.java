package jain.samkit.contacts;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {

    private String st;
    private List<ContactInfo> trans;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView TV1, TV2, TV3, TV4, TV5, TV6, TV7;
        public CardView cardView;

        public ViewHolder(View v) {
            super(v);
            TV1 = (TextView) v.findViewById(R.id.tv1);
            TV2 = (TextView) v.findViewById(R.id.tv2);
            TV3 = (TextView) v.findViewById(R.id.tv3);
            TV4 = (TextView) v.findViewById(R.id.tv4);
            TV5 = (TextView) v.findViewById(R.id.tv5);
            TV6 = (TextView) v.findViewById(R.id.tv6);
            TV7 = (TextView) v.findViewById(R.id.tv7);
            cardView = (CardView) v.findViewById(R.id.card_view1);
        }
    }

    public ContactsAdapter(List<ContactInfo> mTrans, String mst) {
        trans = mTrans;
        st = mst;
    }

    @Override
    public ContactsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.my_card_view, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        String na = trans.get(position).getName().toLowerCase();
        String nu = trans.get(position).getNumber().toLowerCase();
        String em = trans.get(position).getEmail().toLowerCase();
        String or = trans.get(position).getOrganization().toLowerCase();
        String ad = trans.get(position).getAddress().toLowerCase();
        String no = trans.get(position).getNote().toLowerCase();
        String im = trans.get(position).getIm().toLowerCase();

        if(na.contains(st)) {
            int startPos = na.indexOf(st) + (holder.TV1.getResources().getString(R.string.name_title) + " ").length();
            int endPos = startPos + st.length();

            Spannable spanText = Spannable.Factory.getInstance().newSpannable(holder.TV1.getResources().getString(R.string.name_title) + " " + trans.get(position).getName());
            spanText.setSpan(new ForegroundColorSpan(Color.YELLOW), startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            holder.TV1.setText(spanText, TextView.BufferType.SPANNABLE);
        } else {
            if(!na.isEmpty()) {
                holder.TV1.setText(holder.TV1.getResources().getString(R.string.name_title) + " " + trans.get(position).getName());
            } else {
                holder.TV1.setVisibility(View.GONE);
            }
        }

        if(nu.contains(st)) {
            int startPos = nu.indexOf(st) + (holder.TV1.getResources().getString(R.string.number_title) + " ").length();
            int endPos = startPos + st.length();

            Spannable spanText = Spannable.Factory.getInstance().newSpannable(holder.TV1.getResources().getString(R.string.number_title) + " " + trans.get(position).getNumber());
            spanText.setSpan(new ForegroundColorSpan(Color.YELLOW), startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            holder.TV2.setText(spanText, TextView.BufferType.SPANNABLE);
        } else {
            if(!nu.isEmpty()) {
                holder.TV2.setText(holder.TV1.getResources().getString(R.string.number_title) + " " + trans.get(position).getNumber());
            } else {
                holder.TV2.setVisibility(View.GONE);
            }
        }

        if(em.contains(st)) {
            int startPos = em.indexOf(st) + (holder.TV1.getResources().getString(R.string.email_title) + " ").length();
            int endPos = startPos + st.length();

            Spannable spanText = Spannable.Factory.getInstance().newSpannable(holder.TV1.getResources().getString(R.string.email_title) + " " + trans.get(position).getEmail());
            spanText.setSpan(new ForegroundColorSpan(Color.YELLOW), startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            holder.TV3.setText(spanText, TextView.BufferType.SPANNABLE);
        } else {
            if(!em.isEmpty()) {
                holder.TV3.setText(holder.TV1.getResources().getString(R.string.email_title) + " " + trans.get(position).getEmail());
            } else {
                holder.TV3.setVisibility(View.GONE);
            }
        }

        if(or.contains(st)) {
            int startPos = or.indexOf(st) + (holder.TV1.getResources().getString(R.string.organization_title) + " ").length();
            int endPos = startPos + st.length();

            Spannable spanText = Spannable.Factory.getInstance().newSpannable(holder.TV1.getResources().getString(R.string.organization_title) + " " + trans.get(position).getOrganization());
            spanText.setSpan(new ForegroundColorSpan(Color.YELLOW), startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            holder.TV4.setText(spanText, TextView.BufferType.SPANNABLE);
        } else {
            if(!or.isEmpty()) {
                holder.TV4.setText(holder.TV1.getResources().getString(R.string.organization_title) + " " + trans.get(position).getOrganization());
            } else {
                holder.TV4.setVisibility(View.GONE);
            }
        }

        if(ad.contains(st)) {
            int startPos = ad.indexOf(st) + (holder.TV1.getResources().getString(R.string.address_title) + " ").length();
            int endPos = startPos + st.length();

            Spannable spanText = Spannable.Factory.getInstance().newSpannable(holder.TV1.getResources().getString(R.string.address_title) + " " + trans.get(position).getAddress());
            spanText.setSpan(new ForegroundColorSpan(Color.YELLOW), startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            holder.TV5.setText(spanText, TextView.BufferType.SPANNABLE);
        } else {
            if(!ad.isEmpty()) {
                holder.TV5.setText(holder.TV1.getResources().getString(R.string.address_title) + " " + trans.get(position).getAddress());
            } else {
                holder.TV5.setVisibility(View.GONE);
            }
        }

        if(no.contains(st)) {
            int startPos = no.indexOf(st) + (holder.TV1.getResources().getString(R.string.note_title) + " ").length();
            int endPos = startPos + st.length();

            Spannable spanText = Spannable.Factory.getInstance().newSpannable(holder.TV1.getResources().getString(R.string.note_title) + " " + trans.get(position).getNote());
            spanText.setSpan(new ForegroundColorSpan(Color.YELLOW), startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            holder.TV6.setText(spanText, TextView.BufferType.SPANNABLE);
        } else {
            if(!no.isEmpty()) {
                holder.TV6.setText(holder.TV1.getResources().getString(R.string.note_title) + " " + trans.get(position).getNote());
            } else {
                holder.TV6.setVisibility(View.GONE);
            }
        }

        if(im.contains(st)) {
            int startPos = im.indexOf(st) + (holder.TV1.getResources().getString(R.string.im_title) + " ").length();
            int endPos = startPos + st.length();

            Spannable spanText = Spannable.Factory.getInstance().newSpannable(holder.TV1.getResources().getString(R.string.im_title) + " " + trans.get(position).getIm());
            spanText.setSpan(new ForegroundColorSpan(Color.YELLOW), startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            holder.TV7.setText(spanText, TextView.BufferType.SPANNABLE);
        } else {
            if(!im.isEmpty()) {
                holder.TV7.setText(holder.TV1.getResources().getString(R.string.im_title) + " " + trans.get(position).getIm());
            } else {
                holder.TV7.setVisibility(View.GONE);
            }
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.cardView.getContext());
                builder.setMessage(holder.TV1.getResources().getString(R.string.id_title) + " " + trans.get(position).getId() + "\n"
                        + holder.TV1.getResources().getString(R.string.name_title) + " " + trans.get(position).getName() + "\n"
                        + holder.TV1.getResources().getString(R.string.number_title) + " " + trans.get(position).getNumber() + "\n"
                        + holder.TV1.getResources().getString(R.string.email_title) + " " + trans.get(position).getEmail() + "\n"
                        + holder.TV1.getResources().getString(R.string.organization_title) + " " + trans.get(position).getOrganization() + "\n"
                        + holder.TV1.getResources().getString(R.string.address_title) + " " + trans.get(position).getAddress() + "\n"
                        + holder.TV1.getResources().getString(R.string.note_title) + " " + trans.get(position).getNote() + "\n"
                        + holder.TV1.getResources().getString(R.string.im_title) + " " + trans.get(position).getIm() + "\n"
                ).setTitle(holder.TV1.getResources().getString(R.string.dialog_title));
                builder.setNeutralButton(holder.TV1.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //do nothing
                    }
                });
                builder.setCancelable(false);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return trans.size();
    }
}