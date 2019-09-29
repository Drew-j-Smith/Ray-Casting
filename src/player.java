import java.awt.event.KeyEvent;

public class player extends rayCaster {

    private double walkSpd = .5;
    private double runSpd = 1;
    private double strafeSpd = .5;
    private double crouchingModifier = .5;
    private double defaultRenderHeight;

    public player(){
        super();
        defaultRenderHeight = getRenderHeight();
    }


    public void update(boolean[] keyCodes){
        lineseg l = new lineseg();
        if (keyCodes[KeyEvent.VK_A]) {
            l = new lineseg(getCenter(), getCentralAngle() + 90, -strafeSpd);
            setCenter(l.getEp());
        }
        if (keyCodes[KeyEvent.VK_D]) {
            l = new lineseg(getCenter(), getCentralAngle() + 90, strafeSpd);
            setCenter(l.getEp());
        }
        if (keyCodes[KeyEvent.VK_W]){
            if (keyCodes[KeyEvent.VK_CONTROL]) {
                l = new lineseg(getCenter(), getCentralAngle(), runSpd);
            }
            else {
                l = new lineseg(getCenter(), getCentralAngle(), walkSpd);
            }
            setCenter(l.getEp());
        }
        if (keyCodes[KeyEvent.VK_S]){
            l = new lineseg(getCenter(), getCentralAngle(), -walkSpd);
            setCenter(l.getEp());
        }
        if (keyCodes[KeyEvent.VK_SHIFT]) {
            super.setRenderHeight(defaultRenderHeight * crouchingModifier);
        }
        if (!keyCodes[KeyEvent.VK_SHIFT]){
            super.setRenderHeight(defaultRenderHeight);
        }

    }




    public double getWalkSpd() {
        return walkSpd;
    }

    public void setWalkSpd(double walkSpd) {
        this.walkSpd = walkSpd;
    }

    public double getRunSpd() {
        return runSpd;
    }

    public void setRunSpd(double runSpd) {
        this.runSpd = runSpd;
    }

    public double getStrafeSpd() {
        return strafeSpd;
    }

    public void setStrafeSpd(double strafeSpd) {
        this.strafeSpd = strafeSpd;
    }

    public double getCrouchingModifier() {
        return crouchingModifier;
    }

    public void setCrouchingModifier(double crouchingModifier) {
        this.crouchingModifier = crouchingModifier;
    }

    public double getDefaultRenderHeight() {
        return defaultRenderHeight;
    }

    public void setDefaultRenderHeight(double defaultRenderHeight) {
        this.defaultRenderHeight = defaultRenderHeight;
    }

    public void setRenderHeight(double renderHeight) {
        super.setRenderHeight(renderHeight);
        defaultRenderHeight = renderHeight;
    }
}
