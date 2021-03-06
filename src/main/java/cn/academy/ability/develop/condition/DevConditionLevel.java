/**
* Copyright (c) Lambda Innovation, 2013-2016
* This file is part of the AcademyCraft mod.
* https://github.com/LambdaInnovation/AcademyCraft
* Licensed under GPLv3, see project root for more information.
*/
package cn.academy.ability.develop.condition;

import cn.academy.ability.api.Skill;
import cn.academy.ability.api.data.AbilityData;
import cn.academy.ability.develop.IDeveloper;
import net.minecraft.util.ResourceLocation;

/**
 * @author WeAthFolD
 */
public class DevConditionLevel implements IDevCondition {

    @Override
    public boolean accepts(AbilityData data, IDeveloper developer, Skill skill) {
        return data.getLevel() >= skill.getLevel();
    }
    
    @Override
    public ResourceLocation getIcon() {
        return null;
    }

    @Override
    public String getHintText() {
        return null;
    }

    @Override
    public boolean shouldDisplay() {
        return false;
    }

}
