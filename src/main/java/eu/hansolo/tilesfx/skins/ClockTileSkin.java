/*
 * Copyright (c) 2016 by Gerrit Grunwald
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package eu.hansolo.tilesfx.skins;

import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.fonts.Fonts;
import eu.hansolo.tilesfx.tools.Helper;
import javafx.beans.InvalidationListener;
import javafx.geometry.VPos;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;


/**
 * Created by hansolo on 19.12.16.
 */
public class ClockTileSkin extends TileSkin {
    private DateTimeFormatter timeFormatter;
    private DateTimeFormatter dateFormatter;
    private DateTimeFormatter dayOfWeekFormatter;
    private Text              titleText;
    private Text              text;
    private Rectangle         timeRect;
    private Text              timeText;
    private Text              dayOfWeekText;
    private Text              dateText;


    // ******************** Constructors **************************************
    public ClockTileSkin(final Tile TILE) {
        super(TILE);
    }


    // ******************** Initialization ************************************
    @Override protected void initGraphics() {
        super.initGraphics();

        currentValueListener = o -> updateTime(ZonedDateTime.ofInstant(Instant.ofEpochSecond(tile.getCurrentTime()), ZoneId.of(ZoneId.systemDefault().getId())));
        timeListener         = o -> updateTime(tile.getTime());

        timeFormatter      = DateTimeFormatter.ofPattern("HH:mm", tile.getLocale());
        dateFormatter      = DateTimeFormatter.ofPattern("dd MMM YYYY", tile.getLocale());
        dayOfWeekFormatter = DateTimeFormatter.ofPattern("EEEE", tile.getLocale());

        titleText = new Text("");
        titleText.setTextOrigin(VPos.TOP);
        Helper.enableNode(titleText, !tile.getTitle().isEmpty());

        text = new Text(tile.getText());
        text.setFill(tile.getUnitColor());
        Helper.enableNode(text, tile.isTextVisible());

        timeRect = new Rectangle();

        timeText = new Text(timeFormatter.format(tile.getTime()));
        timeText.setTextOrigin(VPos.CENTER);

        dateText = new Text(dateFormatter.format(tile.getTime()));

        dayOfWeekText = new Text(dayOfWeekFormatter.format(tile.getTime()));

        getPane().getChildren().addAll(titleText, text, timeRect, timeText, dateText, dayOfWeekText);
    }

    @Override protected void registerListeners() {
        super.registerListeners();
        if (tile.isAnimated()) {
            tile.currentTimeProperty().addListener(currentTimeListener);
        } else {
            tile.timeProperty().addListener(timeListener);
        }
    }


    // ******************** Methods *******************************************
    @Override protected void handleEvents(final String EVENT_TYPE) {
        super.handleEvents(EVENT_TYPE);
        if ("VISIBILITY".equals(EVENT_TYPE)) {
            Helper.enableNode(titleText, !tile.getTitle().isEmpty());
            Helper.enableNode(text, tile.isTextVisible());
        }
    };

    public void updateTime(final ZonedDateTime TIME) {
        timeText.setText(timeFormatter.format(tile.getTime()));
        timeText.setX((size - timeText.getLayoutBounds().getWidth()) * 0.5);
        timeText.setY(size * 0.4);

        dayOfWeekText.setText(dayOfWeekFormatter.format(TIME));
        dayOfWeekText.setX(size * 0.05);

        dateText.setText(dateFormatter.format(TIME));
        dateText.setX(size * 0.05);
    }

    @Override public void dispose() {
        if (tile.isAnimated()) {
            tile.currentTimeProperty().removeListener(currentTimeListener);
        } else {
            tile.timeProperty().removeListener(timeListener);
        }
        super.dispose();
    }


    // ******************** Resizing ******************************************
    @Override protected void resizeDynamicText() {
        double maxWidth = size * 0.9;
        double fontSize = size * 0.3;

        timeText.setFont(Fonts.latoRegular(fontSize));
        timeText.setText(timeFormatter.format(tile.getTime()));
        Helper.adjustTextSize(timeText, maxWidth, fontSize);
        timeText.setX((size - timeText.getLayoutBounds().getWidth()) * 0.5);
        timeText.setY(size * 0.4);

        maxWidth = size * 0.9;
        fontSize = size * 0.1;
        dayOfWeekText.setFont(Fonts.latoRegular(fontSize));
        if (dayOfWeekText.getLayoutBounds().getWidth() > maxWidth) { Helper.adjustTextSize(dayOfWeekText, maxWidth, fontSize); }
        dayOfWeekText.setX(size * 0.05);
        dayOfWeekText.setY(size * 0.725);

        maxWidth = size * 0.9;
        dateText.setFont(Fonts.latoRegular(fontSize));
        if (dateText.getLayoutBounds().getWidth() > maxWidth) { Helper.adjustTextSize(dateText, maxWidth, fontSize); }
        dateText.setX(size * 0.05);
        dateText.setY(size * 0.85);
    };
    @Override protected void resizeStaticText() {
        double maxWidth = size * 0.9;
        double fontSize = size * textSize.factor;

        titleText.setFont(Fonts.latoRegular(fontSize));
        if (titleText.getLayoutBounds().getWidth() > maxWidth) { Helper.adjustTextSize(titleText, maxWidth, fontSize); }
        titleText.relocate(size * 0.05, size * 0.05);

        maxWidth = size * 0.9;
        fontSize = size * textSize.factor;
        text.setText(tile.getText());
        text.setFont(Fonts.latoRegular(fontSize));
        if (text.getLayoutBounds().getWidth() > maxWidth) { Helper.adjustTextSize(text, maxWidth, fontSize); }
        text.setX(size * 0.05);
        text.setY(size * 0.95);
    };

    @Override protected void resize() {
        super.resize();

        timeRect.setWidth(size);
        timeRect.setHeight(size * 0.4);
        timeRect.setX(0);
        timeRect.setY(size * 0.2);
    };

    @Override protected void redraw() {
        super.redraw();

        titleText.setText(tile.getTitle());
        text.setText(tile.getText());
        timeFormatter      = DateTimeFormatter.ofPattern("HH:mm", tile.getLocale());
        dateFormatter      = DateTimeFormatter.ofPattern("dd MMM YYYY", tile.getLocale());
        dayOfWeekFormatter = DateTimeFormatter.ofPattern("EEEE", tile.getLocale());

        ZonedDateTime time = tile.getTime();

        updateTime(time);

        resizeStaticText();
        resizeDynamicText();

        titleText.setFill(tile.getTitleColor());
        text.setFill(tile.getTextColor());
        timeRect.setFill(tile.getBackgroundColor().darker());
        timeText.setFill(tile.getTitleColor());
        dateText.setFill(tile.getDateColor());
        dayOfWeekText.setFill(tile.getDateColor());
    };
}
