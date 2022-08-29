package me.obsilabor.layercontrol.networking

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import me.obsilabor.layercontrol.config.ClothConfigManager
import me.obsilabor.layercontrol.config.LayerControlConfig
import me.obsilabor.layercontrol.config.SomehowMapsDontWork
import me.obsilabor.layercontrol.json
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking

object NetworkingListener {
    fun init(server: Boolean) {
        // CLIENT SIDE
        if (!server) {
            ClientPlayNetworking.registerGlobalReceiver(Packets.PLS_GIVE) { _, _, byteBuf, _ ->
                runCatching {
                    if (ClothConfigManager.config?.shareWithOthers != true) {
                        return@registerGlobalReceiver
                    } else {
                        ClientPlayNetworking.send(Packets.MY_LAYERS, PacketByteBufs.create().writeUUID(byteBuf.readUUID()).writeUtf(json.encodeToString(ClothConfigManager.config)))
                    }
                }
            }
            ClientPlayNetworking.registerGlobalReceiver(Packets.LAYERS_OF) { _, _, byteBuf, _ ->
                runCatching {
                    val uuid = byteBuf.readUUID()
                    val config = json.decodeFromString<LayerControlConfig>(byteBuf.readUtf())
                    println("Received layers of $uuid")
                    ClothConfigManager.CONFIGS.add(SomehowMapsDontWork(uuid, config))
                    println("Saved layers of $uuid")
                }
            }
            ClientPlayNetworking.registerGlobalReceiver(Packets.INVALIDATE) { _, _, _, _ ->
                ClothConfigManager.clearConfigs()
            }
        }
        // SERVER SIDE
        if (server) {
            ServerPlayNetworking.registerGlobalReceiver(Packets.I_WANT) { server, _, handler, byteBuf, _ ->
                runCatching {
                    val uuid = byteBuf.readUUID()
                    val player = server.playerList.getPlayer(uuid) ?: return@runCatching
                    ServerPlayNetworking.send(player, Packets.PLS_GIVE, PacketByteBufs.create().writeUUID(handler.player.uuid))
                }
            }
            ServerPlayNetworking.registerGlobalReceiver(Packets.MY_LAYERS) { server, _, _, byteBuf, _ ->
                runCatching {
                    val uuid = byteBuf.readUUID()
                    val player = server.playerList.getPlayer(uuid) ?: return@runCatching
                    ServerPlayNetworking.send(player, Packets.LAYERS_OF, PacketByteBufs.create().writeUUID(uuid).writeUtf(byteBuf.readUtf()))
                }
            }
            ServerPlayNetworking.registerGlobalReceiver(Packets.I_CHANGED) { server, _, _, _, _ ->
                runCatching {
                    for (player in server.playerList.players) {
                        ServerPlayNetworking.send(player, Packets.INVALIDATE, PacketByteBufs.empty())
                    }
                }
            }
        }
    }
}