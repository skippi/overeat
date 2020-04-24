var InsnList = Java.type("org.objectweb.asm.tree.InsnList")
var InsnNode = Java.type("org.objectweb.asm.tree.InsnNode")
var VarInsnNode = Java.type("org.objectweb.asm.tree.VarInsnNode")
var Opcodes = Java.type("org.objectweb.asm.Opcodes")


var ALOAD = function (argNo) { return new VarInsnNode(Opcodes.ALOAD, argNo) }
var ICONST_1 = new InsnNode(Opcodes.ICONST_1)
var IRETURN = new InsnNode(Opcodes.IRETURN)



function initializeCoreMod() {
    return {
        'coremodmethod': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.entity.player.PlayerEntity',
                'methodName': 'canEat',
                'methodDesc': '(Z)Z'
            },
            'transformer': function(method) {
                var asmApi = Java.type('net.minecraftforge.coremod.api.ASMAPI');

                var canEatCall = asmApi.buildMethodCall(
                    "io/skippi/overeat/OverEatMod",
                    "canEatWithConfig",
                    "(Lnet/minecraft/entity/player/PlayerEntity;)Z",
                    asmApi.MethodType.STATIC)

                var mixin = new InsnList()
                mixin.add(ALOAD(0))
                mixin.add(canEatCall)
                mixin.add(IRETURN)

                method.instructions.insert(mixin)
                return method;
            }
        }
    }
}