package dev.rico.internal.projector.ui;


import dev.rico.internal.projector.ForRemoval;
import dev.rico.remoting.Property;
import dev.rico.remoting.RemotingBean;

@RemotingBean
@ForRemoval
public class SplitMenuButtonModel extends ButtonModel {
   // TODO: Hack!
   private Property<String> actionA;
   private Property<String> captionA;
   private Property<String> actionB;
   private Property<String> captionB;
    private Property<String> actionC;
    private Property<String> captionC;
    private Property<String> actionD;
    private Property<String> captionD;

   public String getActionA() {
      return actionA.get();
   }

   public void setActionA(String actionA) {
      this.actionA.set(actionA);
   }

   public Property<String> actionAProperty() {
      return actionA;
   }

   public String getCaptionA() {
      return captionA.get();
   }

   public void setCaptionA(String captionA) {
      this.captionA.set(captionA);
   }

   public Property<String> captionAProperty() {
      return captionA;
   }

   public String getActionB() {
      return actionB.get();
   }

   public void setActionB(String actionB) {
      this.actionB.set(actionB);
   }

   public Property<String> actionBProperty() {
      return actionB;
   }

   public String getCaptionB() {
      return captionB.get();
   }

   public void setCaptionB(String captionB) {
      this.captionB.set(captionB);
   }

   public Property<String> captionBProperty() {
      return captionB;
   }

    public String getActionC() {
        return actionC.get();
    }

    public void setActionC(String actionC) {
        this.actionC.set(actionC);
    }

    public Property<String> actionCProperty() {
        return actionC;
    }

    public String getCaptionC() {
        return captionC.get();
    }

    public void setCaptionC(String captionC) {
        this.captionC.set(captionC);
    }

    public Property<String> captionCProperty() {
        return captionC;
    }

    public String getActionD() {
        return actionD.get();
    }

    public void setActionD(String actionD) {
        this.actionD.set(actionD);
    }

    public Property<String> actionDProperty() {
        return actionD;
    }

    public String getCaptionD() {
        return captionD.get();
    }

    public void setCaptionD(String captionD) {
        this.captionD.set(captionD);
    }

    public Property<String> captionDProperty() {
        return captionD;
    }
}
