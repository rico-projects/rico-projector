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
package dev.rico.internal.projector.ui.dialog;


import dev.rico.remoting.Property;
import dev.rico.remoting.RemotingBean;

@RemotingBean
public class InfoDialogModel extends DialogModel {
   private Property<String> headerText;
   private Property<String> contentText;

   public String getHeaderText() {
      return headerText.get();
   }

   public void setHeaderText(String headerText) {
      this.headerText.set(headerText);
   }

   public Property<String> headerTextProperty() {
      return headerText;
   }

   public String getContentText() {
      return contentText.get();
   }

   public void setContentText(String contentText) {
      this.contentText.set(contentText);
   }

   public Property<String> contentTextProperty() {
      return contentText;
   }
}
