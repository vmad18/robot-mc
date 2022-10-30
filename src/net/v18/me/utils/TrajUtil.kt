package net.v18.me.utils

import net.v18.me.utils.math.Vec3D

enum class TrajUtil {
    POS,
    VEL,
    ACC,
    ANGLES, // [yaw, pitch, roll]
    ANGLES_DT,
    ANGULAR,
    ANGULAR_DT
}