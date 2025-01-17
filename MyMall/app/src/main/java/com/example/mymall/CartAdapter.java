package com.example.mymall;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter {

   private List<CartItemModel> cartItemModelList;
   private int lastPosition = -1;
   private TextView cartTotalAmount;
   private boolean showDeleteBtn;

    public CartAdapter(List<CartItemModel> cartItemModelList,TextView cartTotalAmount,boolean showDeleteBtn) {
        this.cartItemModelList = cartItemModelList;
        this.cartTotalAmount = cartTotalAmount;
        this.showDeleteBtn = showDeleteBtn;
    }

    @Override
    public int getItemViewType(int position) {
        switch (cartItemModelList.get(position).getType()){
            case 0:
                return CartItemModel.CART_ITEM;
            case 1:
                return CartItemModel.TOTAL_AMOUNT;
            default:
                return -1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        switch (viewType){
            case CartItemModel.CART_ITEM:
                View cartItemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_item_layout,viewGroup,false);
                return new CartItemViewholder(cartItemView);
            case CartItemModel.TOTAL_AMOUNT:
                View cartTotalView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_total_amount_layout,viewGroup,false);
                return new CartTotalAmountViewholder(cartTotalView);
            default:
                return null;
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        switch (cartItemModelList.get(position).getType()){
            case CartItemModel.CART_ITEM:
                String productId = cartItemModelList.get(position).getProductID();
                String resource = cartItemModelList.get(position).getProductImage();
                String title = cartItemModelList.get(position).getProductTitle();
                Long freeCoupens =cartItemModelList.get(position).getFreeCoupen();
                String productPrice =cartItemModelList.get(position).getProductprice();
                String cuttedPrice = cartItemModelList.get(position).getCuttedPrice();
                Long offersApplied = cartItemModelList.get(position).getOfferApplied();
                boolean inStock = cartItemModelList.get(position).isInStock();

                ((CartItemViewholder)viewHolder).setItemDetails(productId,resource,title,freeCoupens,productPrice,cuttedPrice,offersApplied,position,inStock);
                break;
            case CartItemModel.TOTAL_AMOUNT:
                int totalItems =  0;
                int totalItemPrice = 0;
                String deliveryPrice;
                int totalAmount;
                int savedAmount = 0;

                for(int x =0;x<cartItemModelList.size();x++){
                    if(cartItemModelList.get(x).getType() == CartItemModel.CART_ITEM && cartItemModelList.get(x).isInStock()){
                        totalItems++;
                        totalItemPrice = totalItemPrice +Integer.parseInt(cartItemModelList.get(x).getProductprice());
                    }
                }
                if(totalItemPrice>500){
                    deliveryPrice ="Miễn Phí";
                    totalAmount = totalItemPrice;
                }
                else {
                    deliveryPrice = "10";
                    totalAmount = totalItemPrice + 10;
                }
                cartItemModelList.get(position).setTotalItems(totalItems);
                cartItemModelList.get(position).setTotalItemPrice(totalItemPrice);
                cartItemModelList.get(position).setDeliveryPrice(deliveryPrice);
                cartItemModelList.get(position).setTotalAmount(totalAmount);
                cartItemModelList.get(position).setSavedAmount(savedAmount);

                ((CartTotalAmountViewholder)viewHolder).setTotalAmount(totalItems,totalItemPrice,deliveryPrice,totalAmount,savedAmount);
                break;
            default:
                return;

        }
        if(lastPosition <position){
            Animation animation = AnimationUtils.loadAnimation(viewHolder.itemView.getContext(),R.anim.fade_in);
            viewHolder.itemView.setAnimation(animation);
            lastPosition = position;
        }

    }

    @Override
    public int getItemCount() {
        return cartItemModelList.size();
    }

    class CartItemViewholder extends RecyclerView.ViewHolder{

        private ImageView productImage;
        private ImageView freeCoupenIcon;
        private TextView productTitle;
        private TextView freeCoupens;
        private TextView productPrice;
        private TextView cuttedPrice;
        private TextView offersApplied;
        private TextView coupenApplied;
        private TextView productQuantity;
        private LinearLayout coupenRedemptionLayout;

        private LinearLayout deleteBtn;

        public CartItemViewholder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_image);
            productTitle = itemView.findViewById(R.id.product_title);
            freeCoupenIcon = itemView.findViewById(R.id.free_coupen_icon);
            freeCoupens = itemView.findViewById(R.id.tv_free_coupen);
            productPrice = itemView.findViewById(R.id.product_price);
            cuttedPrice = itemView.findViewById(R.id.cutted_price);
            offersApplied = itemView.findViewById(R.id.offers_applied);
            coupenApplied = itemView.findViewById(R.id.coupens_applied);
            productQuantity = itemView.findViewById(R.id.product_quantity);
            coupenRedemptionLayout = itemView.findViewById(R.id.coupe_redemption_layout);

            deleteBtn = itemView.findViewById(R.id.remove_item_btn);
        }
        @RequiresApi(api = Build.VERSION_CODES.M)
        private void setItemDetails(String productID, String resource, String title, Long freeCoupensNo, String productPriceText, String cuttedPriceText, Long offersAppliedNo, final int position, boolean inStock){

            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.mipmap.profile_placeholder)).into(productImage);
            productTitle.setText(title);

            if(inStock) {
                if(freeCoupensNo > 0){
                    freeCoupenIcon.setVisibility(View.VISIBLE);
                    freeCoupens.setVisibility(View.VISIBLE);
                    freeCoupens.setText(freeCoupensNo + " mã khuyến mãi");
                }
                else {
                    freeCoupenIcon.setVisibility(View.INVISIBLE);
                    freeCoupens.setVisibility(View.INVISIBLE);
                }

                productPrice.setText(productPriceText+" VND");
                productPrice.setTextColor(Color.parseColor("#000000"));
                cuttedPrice.setText(cuttedPriceText+" VND");
                coupenRedemptionLayout.setVisibility(View.VISIBLE);

                productQuantity.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Dialog quantityDialog = new Dialog(itemView.getContext());
                        quantityDialog.setContentView(R.layout.quantity_dialog);
                        quantityDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                        quantityDialog.setCancelable(false);
                        final EditText quantityNo = quantityDialog.findViewById(R.id.quantity_no);
                        Button cancelBtn = quantityDialog.findViewById(R.id.cancel_btn);
                        Button okBtn = quantityDialog.findViewById(R.id.ok_btn);

                        cancelBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                quantityDialog.dismiss();
                            }
                        });
                        okBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                productQuantity.setText("Số lượng: "+quantityNo.getText());
                                quantityDialog.dismiss();
                            }
                        });
                        quantityDialog.show();
                    }
                });
                if (offersAppliedNo > 0){
                    offersApplied.setVisibility(View.VISIBLE);
                    offersApplied.setText("Chấp nhận "+offersAppliedNo);
                }
                else{
                    offersApplied.setVisibility(View.INVISIBLE);
                }
            }else {
                productPrice.setText("Đã hết hàng !");
                productPrice.setTextColor(itemView.getContext().getResources().getColor(R.color.colorPrimary));
                cuttedPrice.setText("");
                coupenRedemptionLayout.setVisibility(View.GONE);
                freeCoupens.setVisibility(View.INVISIBLE);
                productQuantity.setVisibility(View.INVISIBLE);
                coupenApplied.setVisibility(View.GONE);
                offersApplied.setVisibility(View.GONE);
                freeCoupenIcon.setVisibility(View.INVISIBLE);
            }

            if(showDeleteBtn){
                deleteBtn.setVisibility(View.VISIBLE);
            }
            else {
                deleteBtn.setVisibility(View.GONE);
            }

            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!ProductDetailsActivity.running_cart_query){
                        ProductDetailsActivity.running_cart_query = true;
                        DBqueries.removeFromCart(position,itemView.getContext(),cartTotalAmount);
                    }
                }
            });
        }
    }
    class CartTotalAmountViewholder extends RecyclerView.ViewHolder{

        private TextView totalItems;
        private TextView totalItemPrice;
        private TextView deliveryPrice;
        private TextView totalAmount;
        private TextView savedAmount;

        public CartTotalAmountViewholder(@NonNull View itemView) {
            super(itemView);

            totalItems = itemView.findViewById(R.id.total_items);
            totalItemPrice =itemView.findViewById(R.id.total_items_price);
            deliveryPrice = itemView.findViewById(R.id.delivery_price);
            totalAmount = itemView.findViewById(R.id.total_price);
            savedAmount = itemView.findViewById(R.id.saved_amount);
        }
        private  void setTotalAmount(int totalItemText,int totalItemPriceText,String deliveryPricetext,int totalAmountText,int savedAmountText){
            totalItems.setText("Giá ("+totalItemText+")hàng");
            totalItemPrice.setText(totalItemPriceText+" VND");
            if(deliveryPricetext.equals("Miễn Phí")){
                deliveryPrice.setText(deliveryPricetext);
            }else {
                deliveryPrice.setText(deliveryPricetext+ " VND");
            }
            totalAmount.setText(totalAmountText+" VND");
            cartTotalAmount.setText(totalAmountText+" VND");
            savedAmount.setText("Bạn đã tiếp kiệm "+savedAmountText+ " VND trên tổng hóa đơn.");

            LinearLayout parent = (LinearLayout)cartTotalAmount.getParent().getParent();
            if(totalItemPriceText == 0){
                DBqueries.cartItemModelList.remove(DBqueries.cartItemModelList.size()-1);
                parent.setVisibility(View.GONE);
            }else {
                parent.setVisibility(View.VISIBLE);
            }
        }
    }
}
