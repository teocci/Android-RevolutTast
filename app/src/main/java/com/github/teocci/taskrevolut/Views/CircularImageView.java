package com.github.teocci.taskrevolut.Views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import com.github.teocci.taskrevolut.R;
import org.jetbrains.annotations.NotNull;

import static com.github.teocci.taskrevolut.Views.CircularImageView.ShadowGravity.BOTTOM;
import static com.github.teocci.taskrevolut.utils.UtilHelper.minAPI21;
import static com.github.teocci.taskrevolut.utils.UtilHelper.minAPI28;

/**
 * Created by Teocci.
 *
 * @author teocci@yandex.com on 2019-Aug-03
 */
public class CircularImageView extends AppCompatImageView
{
    private static final float DEFAULT_BORDER_WIDTH = 4.0F;
    private static final float DEFAULT_SHADOW_RADIUS = 8.0F;

    private final Paint paint;
    private final Paint paintBorder;
    private final Paint paintBackground;

    private int circleCenter;
    private int heightCircle;
    private int circleColor;
    private int borderColor;
    private int shadowColor;

    private float borderWidth = 0f;
    private float shadowRadius = 0f;

    private boolean shadowEnable = false;

    @NotNull
    private ShadowGravity shadowGravity;
    private ColorFilter civColorFilter;
    private Bitmap civImage;
    private Drawable civDrawable;

    public CircularImageView(@NotNull Context context)
    {
        this(context, null);
    }

    public CircularImageView(@NotNull Context context, @Nullable AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public CircularImageView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);

        paint = new Paint();
        paintBorder = new Paint();
        paintBackground = new Paint();

        paint.setAntiAlias(true);
        paintBorder.setAntiAlias(true);
        paintBackground.setAntiAlias(true);

        circleColor = Color.WHITE;
        borderColor = Color.BLACK;
        shadowColor = Color.BLACK;

        shadowGravity = BOTTOM;

        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr)
    {
        // Load the styled attributes and set their properties
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.CircularImageView, defStyleAttr, 0);

        // Init Background Color
        circleColor = attributes.getColor(R.styleable.CircularImageView_civ_circle_color, Color.WHITE);

        // Init Border
        if (attributes.getBoolean(R.styleable.CircularImageView_civ_border, true)) {
            float defaultBorderSize = DEFAULT_BORDER_WIDTH * getContext().getResources().getDisplayMetrics().density;
            borderWidth = attributes.getDimension(R.styleable.CircularImageView_civ_border_width, defaultBorderSize);
            borderColor = attributes.getColor(R.styleable.CircularImageView_civ_border_color, Color.WHITE);
        }

        // Init Shadow
        shadowEnable = attributes.getBoolean(R.styleable.CircularImageView_civ_shadow, shadowEnable);
        if (shadowEnable) {
            int shadowGravityIntValue = attributes.getInteger(R.styleable.CircularImageView_civ_shadow_gravity, ShadowGravity.BOTTOM.value);
            shadowGravity = ShadowGravity.fromValue(shadowGravityIntValue);
            shadowRadius = attributes.getFloat(R.styleable.CircularImageView_civ_shadow_radius, DEFAULT_SHADOW_RADIUS);
            shadowColor = attributes.getColor(R.styleable.CircularImageView_civ_shadow_color, shadowColor);
        }

        attributes.recycle();
    }

    public void setColorFilter(@NotNull ColorFilter colorFilter)
    {
        civColorFilter = colorFilter;
    }

    @NotNull
    @Override
    public ScaleType getScaleType()
    {
        ScaleType scaleType = super.getScaleType();
        return scaleType == ScaleType.CENTER_INSIDE ? scaleType : ScaleType.CENTER_CROP;
    }

    @Override
    public void setScaleType(@NotNull ScaleType scaleType)
    {
        if (scaleType != ScaleType.CENTER_CROP && scaleType != ScaleType.CENTER_INSIDE) {
            String message = "ScaleType %s not supported. Just ScaleType.CENTER_CROP & ScaleType.CENTER_INSIDE are available for this library.";
            String errorMessage = String.format(message, scaleType);
            throw new IllegalArgumentException(errorMessage);
        } else {
            super.setScaleType(scaleType);
        }
    }

    @Override
    protected void onDraw(@NotNull Canvas canvas)
    {
        loadBitmap();
        if (civImage != null) {
            float circleCenterWithBorder = (float) circleCenter + borderWidth;
            float margeWithShadowRadius = shadowEnable ? shadowRadius * (float) 2 : 0.0F;
            if (shadowEnable) {
                drawShadow();
            }

            canvas.drawCircle(circleCenterWithBorder, circleCenterWithBorder, circleCenterWithBorder - margeWithShadowRadius, paintBorder);
            canvas.drawCircle(circleCenterWithBorder, circleCenterWithBorder, (float) circleCenter - margeWithShadowRadius, paintBackground);
            canvas.drawCircle(circleCenterWithBorder, circleCenterWithBorder, (float) circleCenter - margeWithShadowRadius, paint);
        }
    }

    private final void update()
    {
        if (this.civImage != null) {
            this.updateShader();
        }

        int usableWidth = getWidth() - (getPaddingLeft() + getPaddingRight());
        int usableHeight = getHeight() - (getPaddingTop() + getPaddingBottom());

        heightCircle = Math.min(usableWidth, usableHeight);
        circleCenter = (int) ((float) heightCircle - borderWidth * (float) 2) / 2;
        paintBorder.setColor(borderWidth == 0.0F ? circleColor : borderColor);
        manageElevation();
        invalidate();
    }

    private final void manageElevation()
    {
        if (minAPI21()) {
            setOutlineProvider((!shadowEnable ? new ViewOutlineProvider()
            {
                @Override
                public void getOutline(View view, Outline outline)
                {
                    if (outline != null) {
                        outline.setOval(0, 0, heightCircle, heightCircle);
                    }

                }
            } : null));
        }
    }

    private final void loadBitmap()
    {
        if (civDrawable == getDrawable()) return;

        civDrawable = getDrawable();
        civImage = drawableToBitmap(civDrawable);
        updateShader();
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
        update();
    }

    private final void drawShadow()
    {
        if (minAPI28()) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, paintBorder);
        }

        float dx = 0.0F;
        float dy = 0.0F;
        switch (shadowGravity) {
            case TOP:
                dy = -shadowRadius / (float) 2;
                break;
            case BOTTOM:
                dy = shadowRadius / (float) 2;
                break;
            case START:
                dx = -shadowRadius / (float) 2;
                break;
            case END:
                dx = shadowRadius / (float) 2;
            case CENTER:
            default:
                break;
        }

        paintBorder.setShadowLayer(shadowRadius, dx, dy, shadowColor);
    }

    private final void updateShader()
    {
        if (civImage != null) {
            BitmapShader shader = new BitmapShader(civImage, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            float scale;
            float dx;
            float dy;

            ScaleType scaleType = getScaleType();
            switch (scaleType) {
                case CENTER_CROP:
                    if (civImage.getWidth() * getHeight() > getWidth() * civImage.getHeight()) {
                        scale = (float) getHeight() / (float) civImage.getHeight();
                        dx = ((float) getWidth() - (float) civImage.getWidth() * scale) * 0.5F;
                        dy = 0.0F;
                    } else {
                        scale = (float) getWidth() / (float) civImage.getWidth();
                        dx = 0.0F;
                        dy = ((float) getHeight() - (float) civImage.getHeight() * scale) * 0.5F;
                    }
                    break;
                case CENTER_INSIDE:
                    if (civImage.getWidth() * getHeight() < getWidth() * civImage.getHeight()) {
                        scale = (float) getHeight() / (float) civImage.getHeight();
                        dx = ((float) getWidth() - (float) civImage.getWidth() * scale) * 0.5F;
                        dy = 0.0F;
                    } else {
                        scale = (float) getWidth() / (float) civImage.getWidth();
                        dx = 0.0F;
                        dy = ((float) getHeight() - (float) civImage.getHeight() * scale) * 0.5F;
                    }
                    break;
                default:
                    scale = 0.0F;
                    dx = 0.0F;
                    dy = 0.0F;
            }

            Matrix matrix = new Matrix();
            matrix.setScale(scale, scale);
            matrix.postTranslate(dx, dy);
            shader.setLocalMatrix(matrix);

            // Set Shader in Paint
            paint.setShader(shader);

            // Apply colorFilter
            paint.setColorFilter(civColorFilter);
        }
    }

    private final Bitmap drawableToBitmap(Drawable drawable)
    {
        if (drawable == null) return null;

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else {
            Bitmap bitmap;
            try {
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                drawable.draw(canvas);
            } catch (Exception e) {
                e.printStackTrace();
                bitmap = null;
            }
            return bitmap;
        }
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int width = measure(widthMeasureSpec);
        int height = measure(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    private final int measure(int measureSpec)
    {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        int measure;
        switch (specMode) {
            case MeasureSpec.EXACTLY:
                measure = specSize;
                break;
            case MeasureSpec.AT_MOST:
                measure = specSize;
                break;
            case MeasureSpec.UNSPECIFIED:
            default:
                measure = heightCircle;
                break;
        }

        return measure;
    }


    public final int getCircleColor()
    {
        return circleColor;
    }

    public final void setCircleColor(int circleColor)
    {
        this.circleColor = circleColor;
        paintBackground.setColor(circleColor);
        invalidate();
    }

    public final float getBorderWidth()
    {
        return borderWidth;
    }

    public final void setBorderWidth(float borderWidth)
    {
        this.borderWidth = borderWidth;
        update();
    }

    public final int getBorderColor()
    {
        return borderColor;
    }

    public final void setBorderColor(int borderColor)
    {
        this.borderColor = borderColor;
        update();
    }

    public final float getShadowRadius()
    {
        return shadowRadius;
    }

    public final void setShadowRadius(float shadowRadius)
    {
        this.shadowRadius = shadowRadius;
        setShadowEnable(this.shadowRadius > 0.0F);
    }

    public final int getShadowColor()
    {
        return shadowColor;
    }

    public final void setShadowColor(int shadowColor)
    {
        this.shadowColor = shadowColor;
        invalidate();
    }

    @NotNull
    public final ShadowGravity getShadowGravity()
    {
        return shadowGravity;
    }

    public final void setShadowGravity(@NotNull ShadowGravity shadowGravity)
    {
        this.shadowGravity = shadowGravity;
        invalidate();
    }

    public final boolean getShadowEnable()
    {
        return shadowEnable;
    }

    public final void setShadowEnable(boolean shadowEnable)
    {
        this.shadowEnable = shadowEnable;
        if (this.shadowEnable && shadowRadius == 0.0F) {
            setShadowRadius(DEFAULT_SHADOW_RADIUS);
        }

        update();
    }

    private void setCivColorFilter(ColorFilter civColorFilter)
    {
        if (this.civColorFilter == civColorFilter) return;

        this.civColorFilter = civColorFilter;
        if (this.civColorFilter != null) {
            civDrawable = (Drawable) null;
            invalidate();
        }
    }


    public enum ShadowGravity
    {
        CENTER(1),
        TOP(2),
        BOTTOM(3),
        START(4),
        END(5);

        private final int value;

        ShadowGravity(int value)
        {
            this.value = value;
        }

        public final int getValue()
        {
            return value;
        }

        @NotNull
        public static final ShadowGravity fromValue(int value)
        {
            ShadowGravity shadowGravity;
            switch (value) {
                case 1:
                    shadowGravity = CENTER;
                    break;
                case 2:
                    shadowGravity = TOP;
                    break;
                case 3:
                    shadowGravity = BOTTOM;
                    break;
                case 4:
                    shadowGravity = START;
                    break;
                case 5:
                    shadowGravity = END;
                    break;
                default:
                    throw new IllegalArgumentException("This value is not supported for ShadowGravity: " + value);
            }

            return shadowGravity;
        }
    }
}
