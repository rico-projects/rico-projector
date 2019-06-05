package dev.rico.common.projector.ui;

import dev.rico.remoting.Property;

public interface WithPadding {
     Integer getPadding();

     Property<Integer> paddingProperty() ;

     void setPadding(Integer padding) ;
}