/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2019 The original authors.
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
package dev.rico.internal.projector.ui;


import dev.rico.remoting.Property;
import dev.rico.remoting.RemotingBean;

import java.time.LocalDate;
import java.time.LocalTime;

@RemotingBean
public class DateTimeFieldModel extends ItemModel {
   private Property<Resolution> resolution;
   private Property<String> datePromptText;
   private Property<String> timePromptText;
   private Property<Integer> prefColumnCount;
   private Property<LocalDate> date;
   private Property<LocalTime> time;
   private Property<Boolean> editable;

   public Resolution getResolution() {
      return resolution.get();
   }

   public void setResolution(Resolution resolution) {
      this.resolution.set(resolution);
   }

   public Property<Resolution> resolutionProperty() {
      return resolution;
   }

   public Integer getPrefColumnCount() {
      return prefColumnCount.get();
   }

   public void setPrefColumnCount(Integer prefColumnCount) {
      this.prefColumnCount.set(prefColumnCount);
   }

   public Property<Integer> prefColumnCountProperty() {
      return prefColumnCount;
   }

   public String getDatePromptText() {
      return datePromptText.get();
   }

   public void setDatePromptText(String datePromptText) {
      this.datePromptText.set(datePromptText);
   }

   public Property<String> datePromptTextProperty() {
      return datePromptText;
   }

   public String getTimePromptText() {
      return timePromptText.get();
   }

   public void setTimePromptText(String timePromptText) {
      this.timePromptText.set(timePromptText);
   }

   public Property<String> timePromptTextProperty() {
      return timePromptText;
   }

   public Boolean getEditable() {
      return editable.get();
   }

   public void setEditable(Boolean editable) {
      this.editable.set(editable);
   }

   public Property<Boolean> editableProperty() {
      return editable;
   }

   public LocalTime getTime() {
      return time.get();
   }

   public void setTime(LocalTime time) {
      this.time.set(time);
   }

   public Property<LocalTime> timeProperty() {
      return time;
   }

   public LocalDate getDate() {
      return date.get();
   }

   public void setDate(LocalDate date) {
      this.date.set(date);
   }

   public Property<LocalDate> dateProperty() {
      return date;
   }

   public enum Resolution {Date, Time, DateTime}

}
