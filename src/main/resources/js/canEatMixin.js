var InsnNode = Java.type("org.objectweb.asm.tree.InsnNode")
var Opcodes = Java.type("org.objectweb.asm.Opcodes")

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

                var mixinNodes = asmApi.listOf(
                    ICONST_1,
                    IRETURN
                )

                method.instructions.insert(mixinNodes)
                return method;
            }
        }
    }
}