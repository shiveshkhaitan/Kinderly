package com.hackharvard.desudoers.kinderly;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class Card {
    private String price;
    private String address;
    private Bitmap[] images;
    private String property_id;
    private setBitmapImage set;

    public Card(String price, String address, String property_id, List<String> urls) {
        this.price = price;
        this.address = address;
        this.property_id = property_id;
        this.images = new Bitmap[urls.size()];
        for(int i = 0; i < urls.size(); ++i){
            this.images[i] = null;
            set = new setBitmapImage();
            set.execute(urls.get(i), i+"");
        }
    }

    public String getPrice() { return price; }

    public String getAddress() {
        return address;
    }

    public Bitmap[] getImages() { return images; }

    public String getPropertyId() { return property_id;}

    public void stop(){
        set.cancel(true);
    }

    class setBitmapImage extends AsyncTask<String, Void, Bitmap>  {
        private int index = 0;
        HttpURLConnection httpURLConnection = null;
        URL url = null;
        InputStream inputStream;

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bitmap = null;
            if(set.isCancelled()) {
                return null;
            }
            index = Integer.parseInt(params[1]);
            try {
                url = new URL(params[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                inputStream = httpURLConnection.getInputStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            }
            catch (Exception e){
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            images[index] = result;
        }
    }
}