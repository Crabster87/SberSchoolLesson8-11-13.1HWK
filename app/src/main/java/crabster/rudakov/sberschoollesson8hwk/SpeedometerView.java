package crabster.rudakov.sberschoollesson8hwk;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import androidx.core.content.res.ResourcesCompat;

public class SpeedometerView extends View {

    private final Paint higherZonePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint lowerZonePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int maxSpeed;
    private int speed;
    private String text = getContext().getString(R.string.speed_measure);
    private int color;
    private int textColor;
    private int arrowColor;
    private int markRange;
    private Typeface typeface;

    public SpeedometerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {  //загружаем аттрибуты внутри View
        if (attrs != null) {   //если View создан из Layout
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SpeedometerView);  //то получаем аттрибуты
            CharSequence chars = array.getText(R.styleable.SpeedometerView_android_text);
            text = chars != null ? chars.toString() : text;

            maxSpeed = array.getInt(R.styleable.SpeedometerView_maxSpeed, 100);  //заполняем поля внутри View
            speed = array.getInt(R.styleable.SpeedometerView_speed, 0);
            markRange = array.getInt(R.styleable.SpeedometerView_markRange, 10);
            color = array.getColor(R.styleable.SpeedometerView_color, 0xff98ffb0);
            textColor = array.getColor(R.styleable.SpeedometerView_textColor, 0xff90a0ff);
            arrowColor = array.getColor(R.styleable.SpeedometerView_arrow_color, 0xffff8899);
            typeface = ResourcesCompat.getFont(context, R.font.digital_7_mono_italic);
            higherZonePaint.setTypeface(typeface);

            array.recycle();
        }
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float width = getWidth();
        float height = getHeight();
        float aspect = width / height;
        final float normalAspect = 2f / 1f;
        if (aspect > normalAspect) {
            width = normalAspect * height;
        }
        if (aspect < normalAspect) {
            height = width / normalAspect;
        }

        canvas.save();           //сохраняем состояние канваса
        canvas.translate(width / 2, height);  //сжимаем оси для привычного восприятия
        canvas.scale(.5f * width, -1f * height);

        higherZonePaint.setColor(0x40ffffff);        //белый полупрозрачный цвет
        higherZonePaint.setStyle(Paint.Style.FILL);
        higherZonePaint.setTypeface(typeface);

        drawRingZones(canvas);

        float scale = 0.9f;     //длина штриха деления
        float longScale = 0.9f;
        float textPadding = 0.85f;

        double step = Math.PI / maxSpeed;     //шаг для цены деления шкалы
        drawMarkingOfScale(canvas, scale, longScale, step);
        canvas.restore();

        canvas.save();
        drawScaleAndText(canvas, width, height, scale, longScale, textPadding, step);
        canvas.restore();
        rotateCanvas(canvas, width, height);
        drawArrow(canvas);
    }

    /**
     * Рисуем кольца, очерчивающие зоны циферблата
     */
    private void drawRingZones(Canvas canvas) {
        canvas.drawCircle(0, 0, 1, higherZonePaint);  //подложка для шкалы
        higherZonePaint.setColor(0x20000000);

        canvas.drawCircle(0, 0, 0.8f, higherZonePaint); //нижняя граница шкалы
        higherZonePaint.setColor(color);
        higherZonePaint.setStyle(Paint.Style.STROKE);
        higherZonePaint.setStrokeWidth(0.005f);

        canvas.drawCircle(0, 0, 0.6f, lowerZonePaint); //нижняя граница шкалы
        lowerZonePaint.setColor(color);
        lowerZonePaint.setStrokeWidth(0.005f);
        lowerZonePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        lowerZonePaint.setShader(new LinearGradient(-0.6f, 0.6f, 0.6f, 0.6f, Color.GREEN, Color.RED, Shader.TileMode.CLAMP));
    }

    /**
     * Задаём и отрисовываем цену деления циферблатной сетки
     */
    private void drawMarkingOfScale(Canvas canvas, float scale, float longScale, double step) {
        for (int i = 0; i <= maxSpeed; i = i + 5) {
            float x1 = (float) Math.cos(Math.PI - step * i);   //координаты первой точки(рисуем шкалы в обратную сторону)
            float y1 = (float) Math.sin(Math.PI - step * i);
            float x2;
            float y2;              //координаты второй точки(рисуем шкалы в обратную сторону)
            if (i % markRange == 0) {    //задаем разную длину для значений длины штрихов(кажждого 20-ого)
                x2 = x1 * scale * longScale;
                y2 = y1 * scale * longScale;
            } else {
                x2 = x1 * scale;
                y2 = y1 * scale;
            }
            canvas.drawLine(x1, y1, x2, y2, higherZonePaint); //проводим линию по координатам
        }
    }

    /**
     * Рисуем циферблатную сетку и создаём динамичиский текст со значением текущей скорости
     */
    private void drawScaleAndText(Canvas canvas, float width, float height, float scale, float longScale, float textPadding, double step) {
        canvas.translate(width / 2, 0);  //рисуем шкалу

        higherZonePaint.setTextSize(height / 10);
        higherZonePaint.setColor(textColor);
        higherZonePaint.setStyle(Paint.Style.FILL);

        float factor = height * scale * longScale * textPadding;

        for (int i = 0; i <= maxSpeed; i += markRange) {
            float x = (float) Math.cos(Math.PI - step * i) * factor;
            float y = (float) Math.sin(Math.PI - step * i) * factor;
            String text = Integer.toString(i);
            int textLen = Math.round(higherZonePaint.measureText(text));
            canvas.drawText(Integer.toString(i), x - textLen / 2, height - y, higherZonePaint);
        }

        canvas.drawText(speed + " " + text, -higherZonePaint.measureText(text), height - height * 0.15f, higherZonePaint); //пишем текущее значение скорости
    }

    /**
     * Поворачиваем холст на 90 градусов с целью повернуть стрелку в начальную позицию
     */
    private void rotateCanvas(Canvas canvas, float width, float height) {
        canvas.translate(width / 2, height);
        canvas.scale(.5f * width, -1f * height);
        canvas.rotate(90 - (float) 180 * (speed / (float) maxSpeed));
    }

    /**
     * Рисуем стрелку и её крепление к циферблату
     */
    private void drawArrow(Canvas canvas) {
        higherZonePaint.setColor(arrowColor);  //цвет стрелки
        higherZonePaint.setStrokeWidth(0.02f);
        canvas.drawLine(0.01f, 0, 0, 1f, higherZonePaint);  //стрелка состоит из двух линий
        canvas.drawLine(-0.01f, 0, 0, 1f, higherZonePaint); //немного разнесены от нуля

        higherZonePaint.setStyle(Paint.Style.FILL);
        higherZonePaint.setColor(arrowColor);
        canvas.drawCircle(0f, 0f, .05f, higherZonePaint);  //крепление стрелки
    }

    /**
     * Устанаваливаем размеры Layout
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {  //Layout сообщает View сколько места может занять
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);   //извлекаются размеры, какие от View хочет Layout
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        float relation = width / (float) height;  //соотношение сторон
        final float normalRelation = 2f / 1f;    //необходимое соотношение сторон
        if (relation > normalRelation) {   //если аспект больше, то View растянут по ширине
            if (widthMode != MeasureSpec.EXACTLY) {  //если Layout требует четко определенные размеры от View
                width = Math.round(normalRelation * height); //скорректированное значение
            }
        }
        if (relation < normalRelation) {   //если аспект больше, то View растянут по высоте
            if (heightMode != MeasureSpec.EXACTLY) {
                height = Math.round(width / normalRelation); //скорректированное значение
            }
        }
        setMeasuredDimension(width, height); //Layout передает размер, который может занять, а View - размер, который может использовать
    }

    /**
     * Устанаваливаем максимальную скорость
     */
    public void setMaxSpeed(int maxSpeed) {
        this.maxSpeed = maxSpeed;
        if (speed > maxSpeed) {
            speed = maxSpeed;
        }
        invalidate();
    }

    /**
     * Устанавливаем значение SeekBar'у
     */
    public void setSpeed(int speed) {    //задается значение
        this.speed = Math.min(speed, maxSpeed);
        invalidate();
    }

}