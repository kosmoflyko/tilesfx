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
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;


/**
 * Created by hansolo on 20.12.16.
 */
public class NumberTileSkin extends TileSkin {
    private Text     titleText;
    private Text     text;
    private Text     valueText;
    private Text     unitText;
    private TextFlow valueUnitFlow;
    private Label    description;


    // ******************** Constructors **************************************
    public NumberTileSkin(final Tile TILE) {
        super(TILE);
    }


    // ******************** Initialization ************************************
    @Override protected void initGraphics() {
        super.initGraphics();

        titleText = new Text();
        titleText.setFill(tile.getTitleColor());
        Helper.enableNode(titleText, !tile.getTitle().isEmpty());

        text = new Text(tile.getText());
        text.setFill(tile.getUnitColor());
        Helper.enableNode(text, tile.isTextVisible());

        valueText = new Text(String.format(locale, formatString, ((tile.getValue() - minValue) / range * 100)));
        valueText.setFill(tile.getValueColor());
        valueText.setTextOrigin(VPos.BASELINE);
        Helper.enableNode(valueText, tile.isValueVisible());

        unitText = new Text(" " + tile.getUnit());
        unitText.setFill(tile.getUnitColor());
        unitText.setTextOrigin(VPos.BASELINE);
        Helper.enableNode(unitText, !tile.getUnit().isEmpty());

        valueUnitFlow = new TextFlow(valueText, unitText);
        valueUnitFlow.setTextAlignment(TextAlignment.RIGHT);

        description = new Label(tile.getText());
        description.setAlignment(Pos.TOP_RIGHT);
        description.setWrapText(true);
        description.setTextFill(tile.getTextColor());
        Helper.enableNode(description, tile.isTextVisible());

        getPane().getChildren().addAll(titleText, text, valueUnitFlow, description);
    }

    @Override protected void registerListeners() {
        super.registerListeners();
    }


    // ******************** Methods *******************************************
    @Override protected void handleEvents(final String EVENT_TYPE) {
        super.handleEvents(EVENT_TYPE);

        if ("VISIBILITY".equals(EVENT_TYPE)) {
            Helper.enableNode(titleText, !tile.getTitle().isEmpty());
            Helper.enableNode(text, tile.isTextVisible());
            Helper.enableNode(valueText, tile.isValueVisible());
            Helper.enableNode(unitText, !tile.getUnit().isEmpty());
        }
    };

    @Override protected void handleCurrentValue(final double VALUE) {
        valueText.setText(String.format(locale, formatString, VALUE));
        resizeDynamicText();
    };


    // ******************** Resizing ******************************************
    @Override protected void resizeDynamicText() {
        double maxWidth = unitText.isVisible() ? size * 0.725 : size * 0.9;
        double fontSize = size * 0.24;
        valueText.setFont(Fonts.latoRegular(fontSize));
        if (valueText.getLayoutBounds().getWidth() > maxWidth) { Helper.adjustTextSize(valueText, maxWidth, fontSize); }
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

        maxWidth = size * 0.15;
        fontSize = size * 0.12;
        unitText.setFont(Fonts.latoRegular(fontSize));
        if (unitText.getLayoutBounds().getWidth() > maxWidth) { Helper.adjustTextSize(unitText, maxWidth, fontSize); }

        fontSize = size * 0.1;
        description.setFont(Fonts.latoRegular(fontSize));
    };

    @Override protected void resize() {
        super.resize();

        valueUnitFlow.setPrefWidth(size * 0.9);
        valueUnitFlow.relocate(size * 0.05, size * 0.15);

        description.setPrefSize(size * 0.9, size * 43);
        description.relocate(size * 0.05, size * 0.42);
    };

    @Override protected void redraw() {
        super.redraw();
        titleText.setText(tile.getTitle());
        text.setText(tile.getText());
        valueText.setText(String.format(locale, formatString, tile.getCurrentValue()));
        unitText.setText(tile.getUnit());
        description.setText(tile.getDescription());

        resizeDynamicText();
        resizeStaticText();

        titleText.setFill(tile.getTitleColor());
        text.setFill(tile.getTextColor());
        valueText.setFill(tile.getValueColor());
        unitText.setFill(tile.getUnitColor());
        description.setTextFill(tile.getDescriptionColor());
    };
}
