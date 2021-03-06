/*
 * Copyright 2015-2016 Red Hat, Inc, and individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.hal.ballroom.form;

import java.util.EnumSet;

import com.google.common.base.Strings;
import elemental2.dom.HTMLElement;
import elemental2.dom.HTMLInputElement;
import org.jboss.gwt.elemento.core.Elements;

import static org.jboss.gwt.elemento.core.Elements.button;
import static org.jboss.gwt.elemento.core.Elements.i;
import static org.jboss.gwt.elemento.core.Elements.input;
import static org.jboss.gwt.elemento.core.Elements.span;
import static org.jboss.gwt.elemento.core.EventType.bind;
import static org.jboss.gwt.elemento.core.EventType.change;
import static org.jboss.gwt.elemento.core.EventType.click;
import static org.jboss.gwt.elemento.core.InputType.password;
import static org.jboss.hal.ballroom.form.Decoration.*;
import static org.jboss.hal.ballroom.form.Form.State.EDITING;
import static org.jboss.hal.resources.CSS.*;
import static org.jboss.hal.resources.UIConstants.MASK_CHARACTER;

public class PasswordItem extends AbstractFormItem<String> {

    private static final String FA_EYE_SLASH = "fa-eye-slash";
    private static final String FA_EYE = "fa-eye";

    private final HTMLInputElement inputElement;

    public PasswordItem(String name, String label) {
        super(name, label, null);

        // read-only appearance
        addAppearance(Form.State.READONLY, new PasswordReadOnlyAppearance());

        // editing appearance
        inputElement = input(password).css(formControl).asElement();
        addAppearance(EDITING, new PasswordEditingAppearance(inputElement));
    }

    @Override
    public void attach() {
        super.attach();
        remember(bind(inputElement, change, event -> modifyValue(inputElement.value)));
    }

    @Override
    public boolean isEmpty() {
        return Strings.isNullOrEmpty(getValue());
    }

    @Override
    public boolean supportsExpressions() {
        return false;
    }


    @SuppressWarnings({"ReplaceAllDot", "HardCodedStringLiteral"})
    private class PasswordReadOnlyAppearance extends ReadOnlyAppearance<String> {

        private HTMLElement peekLink;
        private boolean hidden;

        @SuppressWarnings("DuplicateStringLiteralInspection")
        PasswordReadOnlyAppearance() {
            super(EnumSet.of(DEFAULT, DEPRECATED, RESTRICTED));
            hidden = true;
            peekLink = span().css(fontAwesome("eye"), clickable)
                    .title(CONSTANTS.showSensitive())
                    .on(click, event -> {
                        if (hidden) {
                            valueElement.textContent = getValue();
                            peekLink.title = CONSTANTS.hideSensitive();
                            peekLink.classList.add(FA_EYE_SLASH);
                            peekLink.classList.remove(FA_EYE);
                        } else {
                            valueElement.textContent = getValue().replaceAll(".", MASK_CHARACTER);
                            peekLink.title = CONSTANTS.showSensitive();
                            peekLink.classList.add(FA_EYE);
                            peekLink.classList.remove(FA_EYE_SLASH);
                        }
                        hidden = !hidden;
                    })
                    .asElement();
        }

        @Override
        protected String name() {
            return "PasswordReadOnlyAppearance";
        }

        @Override
        public void showValue(String value) {
            if (isEmpty()) {
                Elements.failSafeRemove(valueContainer, peekLink);
            } else {
                valueContainer.appendChild(peekLink);
            }
            super.showValue(value);
        }

        @Override
        public String asString(String value) {
            if (hidden) {
                return value.replaceAll(".", MASK_CHARACTER);
            } else {
                return value;
            }
        }
    }


    private class PasswordEditingAppearance extends EditingAppearance<String> {

        private HTMLElement peekButton;
        private HTMLElement peekIcon;

        @SuppressWarnings({"HardCodedStringLiteral", "DuplicateStringLiteralInspection"})
        PasswordEditingAppearance(HTMLInputElement inputElement) {
            super(EnumSet.of(DEFAULT, DEPRECATED, ENABLED, INVALID, REQUIRED, RESTRICTED), inputElement);
            HTMLElement peekContainer = span().css(inputGroupBtn)
                    .add(peekButton = button().css(btn, btnDefault)
                            .title(CONSTANTS.showSensitive())
                            .on(click, event -> {
                                if ("password".equals(inputElement.type)) {
                                    inputElement.type = "text";
                                    inputElement.focus();
                                    peekButton.title = CONSTANTS.hideSensitive();
                                    peekIcon.classList.add(FA_EYE_SLASH);
                                    peekIcon.classList.remove(FA_EYE);

                                } else if ("text".equals(inputElement.type)) {
                                    inputElement.type = "password";
                                    inputElement.focus();
                                    peekButton.title = CONSTANTS.showSensitive();
                                    peekIcon.classList.add(FA_EYE);
                                    peekIcon.classList.remove(FA_EYE_SLASH);
                                }
                            })
                            .add(peekIcon = i().css(fontAwesome("eye")).asElement())
                            .asElement())
                    .asElement();

            wrapInputElement();
            inputGroup.appendChild(peekContainer);
        }

        @Override
        protected String name() {
            return "PasswordEditingAppearance";
        }

        @Override
        public void showValue(String value) {
            inputElement.value = value;
        }

        @Override
        public void clearValue() {
            inputElement.value = "";
        }
    }
}
