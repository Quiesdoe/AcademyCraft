/**
* Copyright (c) Lambda Innovation, 2013-2016
* This file is part of the AcademyCraft mod.
* https://github.com/LambdaInnovation/AcademyCraft
* Licensed under GPLv3, see project root for more information.
*/
package cn.academy.energy.api.event.wen;

import cn.academy.energy.api.block.IWirelessMatrix;
import cn.academy.energy.api.event.WirelessEvent;

/**
 * Fire this whenever you want to destroy a wireless network manually.
 * NOTE: If the tile is no longer available, it will be removed automatically.
 * @author WeathFolD
 */
public class DestroyNetworkEvent extends WirelessEvent {
    
    public final IWirelessMatrix mat;
    
    public DestroyNetworkEvent(IWirelessMatrix _mat) {
       super(_mat);
       mat = _mat;
    }

}
