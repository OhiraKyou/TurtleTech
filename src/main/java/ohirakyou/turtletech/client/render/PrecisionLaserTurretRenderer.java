package ohirakyou.turtletech.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ohirakyou.turtletech.client.render.util.*;
import ohirakyou.turtletech.common.block.turrets.precisionlaserturret.PrecisionLaserTileEntity;
import ohirakyou.turtletech.common.block.turrets.precisionlaserturret.PrecisionLaserTurretBlock;
import ohirakyou.turtletech.data.DataModInfo;
import ohirakyou.turtletech.util.MathUtils;
import ohirakyou.turtletech.util.RelativePosition;
import ohirakyou.turtletech.util.Size3D;
import org.lwjgl.opengl.GL11;


@SideOnly(Side.CLIENT)
public class PrecisionLaserTurretRenderer extends TileEntitySpecialRenderer<PrecisionLaserTileEntity>{
    private static final ResourceLocation texture = new ResourceLocation(DataModInfo.MOD_ID +
            ":textures/entity/model_precision_laser_turret.png");


    private static final TextureTile BLANK_SIDE_COORDS = new TextureTile(0, 0.5f, 0.5f, 1f);
    private static final TextureTile BLANK_SIDE_COORDS_HORIZONTAL_PIXELS = new TextureTile(0, 0.5f, 0.75f, 1f / 32f + 0.75f);
    private static final TextureTile SIDE_COORDS_ACTIVE = new TextureTile(0.5f, 1f, 0, 0.5f);
    private static final TextureTile SIDE_COORDS_INACTIVE = new TextureTile(0, 0.5f, 0, 0.5f);
    private static final TextureTile CORE_COORDS_ACTIVE = new TextureTile(0.5f, 1f, 0.5f, 0.75f);
    private static final TextureTile CORE_COORDS_INACTIVE = new TextureTile(0.5f, 1f, 0.75f, 1f);

    private static final Tessellator tessellator = Tessellator.getInstance();
    private static final VertexBuffer vertexBuffer = tessellator.getBuffer();


    @Override
    public void renderTileEntityAt(
            final PrecisionLaserTileEntity te, final double x, final double y, final double z,
            final float partialTick, int destroyStage) {

        // partialTick is guaranteed to range from 0 to 1
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)x, (float)y, (float)z);

        render(partialTick, te);

        GlStateManager.popMatrix();

    }


    private void render(float partialTick, PrecisionLaserTileEntity te){
        World world = te.getWorld();
        BlockPos blockPosition = te.getPos();

        if (!(world.getBlockState(blockPosition).getBlock() instanceof PrecisionLaserTurretBlock)) {
            return;
        }

        this.bindTexture(texture);

        RenderHelper.disableStandardItemLighting();
        GlStateManager.blendFunc(770, 771);
        GlStateManager.enableBlend();

        if (Minecraft.isAmbientOcclusionEnabled()) { GlStateManager.shadeModel(7425); }
        else { GlStateManager.shadeModel(7424); }

        float interpolatedYaw = MathUtils.lerp(te.previousYaw, te.currentYaw, partialTick);
        float interpolatedPitch = MathUtils.lerp(te.previousPitch, te.currentPitch, partialTick);

        float yaw = -1 * (interpolatedYaw + te.getYaw());

        GlStateManager.translate(0.5f, 0.5f, 0.5f);

        GlStateManager.disableCull();
        renderBase(te);
        GlStateManager.enableCull();

//        GlStateManager.popMatrix();
//        GlStateManager.pushMatrix();

        GlStateManager.rotate(yaw, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(interpolatedPitch, 1.0f, 0.0f, 0.0f);


        renderBarrel(partialTick, te);


        //renderLaser(te);

        /*
        IModel model = ModelLoaderRegistry.getMissingModel();
        try {
            String modelPath = "block/precision_laser_turret.obj";
            model = ModelLoaderRegistry.getModel(new ResourceLocation(DataModInfo.MOD_ID.toLowerCase() + ":" + modelPath));
        } catch (IOException e) {
            model = ModelLoaderRegistry.getMissingModel();
        }
        */

        RenderHelper.enableStandardItemLighting();
    }


    private void renderBase(PrecisionLaserTileEntity te) {
        final float pixelScale = 16f;

        // Config
        float baseLength = 16f;
        float baseWidth = 16f;
        float baseHeight = 1f;
        float baseShaftHeight = 8f - baseHeight;
        float baseShaftRadius = 2f;

        // Pixel scale application
        baseLength /= pixelScale;
        baseWidth /= pixelScale;
        baseHeight /= pixelScale;
        baseShaftHeight /= pixelScale;
        baseShaftRadius /= pixelScale;

        // Transform config processing
        Size3D baseSize = new Size3D(baseLength, baseWidth, baseHeight);
        Size3D baseShaftSize = new Size3D(baseShaftRadius, baseShaftRadius, baseShaftHeight);

        RelativePosition baseRelativeDirection = getBaseDirection(te);
        float baseDirection = baseRelativeDirection.isUp() ? 1 : -1;
        float baseVertical = 0.5f - baseHeight / 2f;
        float baseShaftVertical = baseShaftHeight / 2f;
        RelativePosition basePosition = new RelativePosition(0, 0, baseVertical * baseDirection);
        RelativePosition baseShaftPosition = new RelativePosition(0, 0, baseShaftVertical * baseDirection);

        // Cube maps
        TexturedCubeMap baseCubeMap = new TexturedCubeMap(BLANK_SIDE_COORDS_HORIZONTAL_PIXELS);
        baseCubeMap.upTile = BLANK_SIDE_COORDS;
        baseCubeMap.downTile = BLANK_SIDE_COORDS;

        TexturedCubeMap baseShaftCubeMap = new TexturedCubeMap(BLANK_SIDE_COORDS);
        baseShaftCubeMap.downTile = null;
        baseShaftCubeMap.upTile = null;


        // Rendering
        vertexBuffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

        if (baseRelativeDirection.isUp() || baseRelativeDirection.isDown()) {
            RenderUtils.addCube(vertexBuffer, baseCubeMap, baseSize, basePosition);
            RenderUtils.addCube(vertexBuffer, baseShaftCubeMap, baseShaftSize, baseShaftPosition);
        }

        tessellator.draw();
    }


    private void renderBarrel(float partialTick, PrecisionLaserTileEntity te) {
        final float pixelScale = 16f;

        // Config
        float headDiameter = 6f;
        float barrelStart = headDiameter / 2f;   // do not change
        float coreRadius = 2f;

        float detailSpacing = 3f;

        float innerDetailLength = 5f;
        float innerDetailWidth = 2f;
        float innerDetailHeight = headDiameter - 1f;
        float innerDetailForward = barrelStart + innerDetailLength / 2f;   // do not change

        float middleCoverLength = innerDetailLength;
        float middleCoverHeight = coreRadius;
        float middleCoverForward = innerDetailForward;

        float endDetailLength = 4f;
        float endDetailHeight = innerDetailWidth;
        float endDetailForward = barrelStart + innerDetailLength + detailSpacing + endDetailLength / 2f;   // do not change

        float barrelLength = innerDetailLength + detailSpacing + endDetailLength;
        Size3D coreSize = new Size3D(barrelLength + 1f, coreRadius, coreRadius);


        float topBottomCoverLength = coreSize.length - 1f;
        float topBottomCoverWidth = coreSize.width + 1f;
        float topBottomCoverHeight = 1f;
        float topBottomCoverForward = barrelStart + topBottomCoverLength / 2f;
        float topBottomCoverUp = coreSize.height / 2 + topBottomCoverHeight / 2f;

        float endDetailWidth = coreSize.width + 2f;

        float middleCoverWidth = innerDetailHeight;


        // Pixel scale application
        headDiameter /= pixelScale;
        barrelStart /= pixelScale;
        coreRadius /= pixelScale;

        topBottomCoverLength /= pixelScale;
        topBottomCoverWidth /= pixelScale;
        topBottomCoverHeight /= pixelScale;
        topBottomCoverForward /= pixelScale;
        topBottomCoverUp /= pixelScale;

        innerDetailLength /= pixelScale;
        innerDetailWidth /= pixelScale;
        innerDetailHeight /= pixelScale;
        innerDetailForward /= pixelScale;

        middleCoverLength /= pixelScale;
        middleCoverWidth /= pixelScale;
        middleCoverHeight /= pixelScale;
        middleCoverForward /= pixelScale;

        endDetailLength /= pixelScale;
        endDetailWidth /= pixelScale;
        endDetailHeight /= pixelScale;
        endDetailForward /= pixelScale;

        barrelLength /= pixelScale;
        coreSize = coreSize.divideBy(pixelScale);



        // Transform config processing
        RelativePosition corePosition = new RelativePosition(barrelStart + coreSize.length / 2f, 0, 0);
        RelativePosition topCoverPosition = new RelativePosition(topBottomCoverForward, 0, topBottomCoverUp);
        RelativePosition bottomCoverPosition = new RelativePosition(topBottomCoverForward, 0, -topBottomCoverUp);
        RelativePosition innerDetailPosition = new RelativePosition(innerDetailForward, 0, 0);
        RelativePosition middleCoverPosition = new RelativePosition(middleCoverForward, 0, 0);
        RelativePosition endDetailPosition = new RelativePosition(endDetailForward, 0, 0);


        Size3D topBottomCoverSize = new Size3D(topBottomCoverLength, topBottomCoverWidth, topBottomCoverHeight);
        Size3D innerDetailSize = new Size3D(innerDetailLength, innerDetailWidth, innerDetailHeight);
        Size3D middleCoverSize = new Size3D(middleCoverLength, middleCoverWidth, middleCoverHeight);
        Size3D endDetailSize = new Size3D(endDetailLength, endDetailWidth, endDetailHeight);


        // Texture tile coordinates
        TextureTile sideCoords = te.isActive() ? SIDE_COORDS_ACTIVE : SIDE_COORDS_INACTIVE;
        TextureTile barrelCoords = te.isActive() ? CORE_COORDS_ACTIVE : CORE_COORDS_INACTIVE;


        // Optimized cube maps
        TexturedCubeMap headCubeMap = new TexturedCubeMap(sideCoords);
        headCubeMap.northTile = BLANK_SIDE_COORDS;
        headCubeMap.upTile = BLANK_SIDE_COORDS;
        headCubeMap.downTile = BLANK_SIDE_COORDS;

        TexturedCubeMap coreCubeMap = new TexturedCubeMap(barrelCoords);
        coreCubeMap.southTile = null;  // not rendered

        TexturedCubeMap barrelCubeMap = new TexturedCubeMap(BLANK_SIDE_COORDS);
        TexturedCubeMap barrelBacklessCubeMap = new TexturedCubeMap(BLANK_SIDE_COORDS);
        barrelBacklessCubeMap.southTile = null;  // not rendered

        TexturedCubeMap barrelFrontAndSidesCubeMap = new TexturedCubeMap(BLANK_SIDE_COORDS);
        barrelFrontAndSidesCubeMap.southTile = null;
        barrelFrontAndSidesCubeMap.upTile = null;
        barrelFrontAndSidesCubeMap. downTile = null;



        // Lit parts
        vertexBuffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

        // Head
        RenderUtils.addCube(vertexBuffer, headCubeMap, new Size3D(headDiameter));

        // Details
        RenderUtils.addCube(vertexBuffer, barrelBacklessCubeMap, topBottomCoverSize, topCoverPosition);
        RenderUtils.addCube(vertexBuffer, barrelBacklessCubeMap, topBottomCoverSize, bottomCoverPosition);
        RenderUtils.addCube(vertexBuffer, barrelBacklessCubeMap, innerDetailSize, innerDetailPosition);
        RenderUtils.addCube(vertexBuffer, barrelBacklessCubeMap, middleCoverSize, middleCoverPosition);
        RenderUtils.addCube(vertexBuffer, barrelCubeMap, endDetailSize, endDetailPosition);

        tessellator.draw();



        // Unlit / lightmapped parts
        GlStateManager.disableLighting();
        vertexBuffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);

        // Laser
        float interpolatedLaserLife = MathUtils.lerp(te.previousLaserLife, te.currentLaserLife, partialTick);

        if (interpolatedLaserLife > 0) {
            float laserStart = barrelStart + barrelLength;
            float laserRoll = MathUtils.lerp(te.previousLaserRotation, te.currentLaserRotation, partialTick);

            float laserLifeLevel = interpolatedLaserLife / te.LASER_LIFE;
            float laserRadiusMultiplier = MathUtils.interpolateAroundThreshold(laserLifeLevel, 0.7f, 0.15f, 2f);
            float laserLength = (float)te.laserLength - laserStart;
            float laserStartRadius = coreRadius * laserRadiusMultiplier;
            float laserEndRadius = laserStartRadius * MathUtils.clamp01(1f - (float)te.laserLength / te.forwardRange);

            RenderUtils.addBeam(vertexBuffer, new RelativePosition(laserStart, 0, 0), laserLength,
                    laserStartRadius, laserEndRadius, 4, laserRoll, CORE_COORDS_ACTIVE, true);
        }

        // Core
        GlStateManager.disableCull();  // gives the laser beam the illusion of volume
        RenderUtils.addCube(vertexBuffer, coreCubeMap, coreSize, corePosition, true);

        tessellator.draw();
        GlStateManager.enableLighting();

    }

    private RelativePosition getBaseDirection(PrecisionLaserTileEntity te) {
        BlockPos thisPos = te.getPos();
        BlockPos upPos = MathUtils.getUpPos(thisPos);
        BlockPos downPos = MathUtils.getDownPos(thisPos);

        RelativePosition relativePosition = new RelativePosition();

        if (!getWorld().isAirBlock(downPos)) {
            relativePosition.upward = -1;
        } else if (!getWorld().isAirBlock(upPos)) {
            relativePosition.upward = 1;
        }

        return relativePosition;
    }

}