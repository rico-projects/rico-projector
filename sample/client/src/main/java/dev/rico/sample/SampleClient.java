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
package dev.rico.sample;

import dev.rico.client.remoting.AbstractRemotingApplication;
import dev.rico.client.remoting.ClientContext;
import dev.rico.internal.client.projector.mixed.ClientContextHolder;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.MalformedURLException;
import java.net.URL;

import static dev.rico.sample.SampleConstants.CONTROLLER_NAME;

public class SampleClient extends AbstractRemotingApplication {

    @Override
    protected URL getServerEndpoint() throws MalformedURLException {
        return new URL("http://localhost:8080/remoting");
    }

    @Override
    protected void start(final Stage primaryStage, final ClientContext clientContext) throws Exception {

        ClientContextHolder.setContext(clientContext);

        SampleViewPresenter presenter = new SampleViewPresenter(CONTROLLER_NAME);

        final Scene scene = new Scene((Parent) presenter.getView());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
