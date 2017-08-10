package com.example.chileme.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chileme.AsyncImageLoader;
import com.example.chileme.MainFragment;
import com.example.chileme.R;
import com.example.chileme.vo.AllOrder;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Wang Xu on 2017/8/9.
 */

public class AllOrderAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<AllOrder> list;
    private ImageView img;
    private Context mContext;
    private ViewHolder holder;
    Handler handler;
    int count = 0;
    private AsyncImageLoader asyncImageLoader=new AsyncImageLoader();
    private String url0 ="http://192.168.137.1:8080/practice2/upload/";

    private OkHttpClient okHttpClient = new OkHttpClient();
    public AllOrderAdapter(Context context, List<AllOrder> data) {
        mContext = context;
        inflater = LayoutInflater.from(context);
        this.list = data;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            v = inflater.inflate(R.layout.list_item1, parent, false);
            holder = new ViewHolder(v);
            v.setTag(holder);
            holder.button = (Button) v.findViewById(R.id.text15);
            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Request request = new Request.Builder()
                            .url("http://192.168.137.1:8080/practice2/order_enterOrder")
                            .get()
                            .build();
                    okHttpClient.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            //失败
                            Log.i("异常：","--->"+e);
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            //成功
                            Log.i("成功：","--->");
                            Intent intent =new Intent(mContext,MainFragment.class);
                            intent.putExtra("flag",3);

                            mContext.startActivity(intent);
                        }
                    });
                }
            });
        } else {
            holder = (ViewHolder) v.getTag();
        }

        holder.storename.setText(list.get(position).getStore_name()+" >");
        holder.foodname.setText(list.get(position).getFood_name()+" 等"+list.get(position).getTotalCount()+"件商品");
        holder.totalprice.setText("￥"+list.get(position).getTotalPrice());
        if(list.get(position).isState()==true){
            holder.orderstate.setText("订单");
            holder.button.setText("再来一单");
            holder.button.setClickable(false);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

//                    holder.img.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            count++;
//                            Log.i("cccc","count=."+count+"position="+position);
//
//                            Message msg=handler.obtainMessage();//子线程中的handle用来给主线程传递消息,虽然本例中的msg没有使用,
//                           // 但是主线程中的handle也要等待该Message,否则不会继续,然后更新UI
//                            msg.what=1;
//                            msg.obj = bitmap;
//                                                   handler.sendMessage(msg);
//
//                        }
//                    });
                }
            }).start();

        }
        else{
            holder.orderstate.setText("商家配送中");
            holder.button.setText("确认送达");
            holder.button.setClickable(true);
        }

//        if (list.get(position)!= Model.NONE) {
//        } else {
//            holder.radioGroup.clearCheck();
//
//        }
        final String url=url0+list.get(position).getPhoto_source();
        //final Bitmap bitmap=getPicture(url);
        asyncImageLoader.loadDrawable(position, url, new AsyncImageLoader.ImageCallback() {
            @Override
            public void onImageLoad(Integer t, Drawable drawable) {
                holder.img.setImageDrawable(drawable);
            }

            @Override
            public void onError(Integer t) {
                Log.i("---","加载失败");
            }
        });

         return v;
    }

    private class ViewHolder {
        ImageView img=null;
        TextView storename = null;
        TextView foodname = null;
        TextView totalprice = null;
        TextView orderstate = null;
        Button button;

        ViewHolder(View v) {
            img=(ImageView)v.findViewById(R.id.item_img);
            storename=(TextView)v.findViewById(R.id.text11);
            foodname=(TextView)v.findViewById(R.id.text12);
            totalprice=(TextView)v.findViewById(R.id.text13);
            orderstate=(TextView)v.findViewById(R.id.text14);
            button=(Button)v.findViewById(R.id.text15);
        }

    }
    public Bitmap getPicture(String path){
        Bitmap bm=null;
        try{
            URL url=new URL(path);
            URLConnection connection=url.openConnection();
            connection.connect();
            InputStream inputStream=connection.getInputStream();
            bm= BitmapFactory.decodeStream(inputStream);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  bm;
    }

}
