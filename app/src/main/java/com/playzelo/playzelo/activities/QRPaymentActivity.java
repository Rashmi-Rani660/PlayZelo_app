package com.playzelo.playzelo.activities;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.playzelo.playzelo.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;

public class QRPaymentActivity extends BaseActivity {

    private TextView tvOrderId, tvAmount, tvTimeShort, tvDateTime;
    private ImageView imgQr;
    private EditText etUtr,etAccount;
    private Button btnCopy, btnSubmit;

    // Example data (replace from API)
    private String orderId = "250820124534716388";
    private String amountDisplay = "₹ 10000";
    private String accountId = "boim-418211660652@boi";
    private String qrPayload = "upi://pay?pa=" + "boim-418211660652@boi" + "&pn=Antpay&am=10000&cu=INR&tn=Order%20" + "250820124534716388";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrpayment);

        bindViews();
        fillHeader();
        renderQr(qrPayload);

        btnCopy.setOnClickListener(v -> {
            copyToClipboard(accountId);
            Toast.makeText(this, "Account copied", Toast.LENGTH_SHORT).show();
        });

        btnSubmit.setOnClickListener(v -> {
            String utr = etUtr.getText().toString().trim();
            if (utr.length() < 6) {
                Toast.makeText(this, "Enter valid UTR/Ref number", Toast.LENGTH_SHORT).show();
                return;
            }
            // TODO: hit API to confirm payment with orderId + utr
            Toast.makeText(this, "Submitted UTR: " + utr, Toast.LENGTH_LONG).show();
        });
    }

    private void bindViews() {
        tvOrderId = findViewById(R.id.tvOrderId);
        tvAmount = findViewById(R.id.tvAmount);
        tvTimeShort = findViewById(R.id.tvTimeShort);
        tvDateTime = findViewById(R.id.tvDateTime);
        etAccount = findViewById(R.id.etAccount);
        imgQr = findViewById(R.id.imgQr);
        etUtr = findViewById(R.id.etUtr);
        btnCopy = findViewById(R.id.btnCopy);
        btnSubmit = findViewById(R.id.btnSubmit);
    }

    private void fillHeader() {
        tvOrderId.setText(orderId);
        tvAmount.setText(amountDisplay);

        String nowDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        String nowTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
        tvDateTime.setText(nowDate);
        tvTimeShort.setText(nowTime);

        etAccount.setText("account  " + accountId);
    }

    private void copyToClipboard(String text) {
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        if (cm != null) {
            cm.setPrimaryClip(ClipData.newPlainText("account", text));
        }
    }

    /** Generate QR bitmap using ZXing and set to ImageView */
    private void renderQr(String data) {
        try {
            Bitmap bmp = generateQrBitmap(data, 1024);
            imgQr.setImageBitmap(bmp);
        } catch (WriterException e) {
            Toast.makeText(this, "QR error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public static Bitmap generateQrBitmap(String data, int size) throws WriterException {
        if (TextUtils.isEmpty(data)) data = "EMPTY";
        QRCodeWriter writer = new QRCodeWriter();
        Map<EncodeHintType, Object> hints = new EnumMap<>(EncodeHintType.class);
        hints.put(EncodeHintType.MARGIN, 1);
        BitMatrix bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, size, size, hints);

        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        int[] pixels = new int[width * height];

        int black = 0xFF000000;
        int white = 0xFFFFFFFF;

        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = bitMatrix.get(x, y) ? black : white;
            }
        }

        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bmp.setPixels(pixels, 0, width, 0, 0, width, height);
        return bmp;
    }
}
