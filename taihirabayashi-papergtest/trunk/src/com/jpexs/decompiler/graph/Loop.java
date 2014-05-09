/*
 *  Copyright (C) 2010-2014 JPEXS
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.jpexs.decompiler.graph;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author JPEXS
 */
public class Loop implements Serializable {

    public GraphPart loopContinue;
    public GraphPart loopBreak;
    public GraphPart loopPreContinue;
    public List<GraphPart> breakCandidates = new ArrayList<>();
    public List<Integer> breakCandidatesLevels = new ArrayList<>();
    public long id;
    public int leadsToMark;
    public int reachableMark;
    public int phase;
    public int breakCandidatesLocked = 0;

    public Loop(long id, GraphPart loopContinue, GraphPart loopBreak) {
        this.loopContinue = loopContinue;
        this.loopBreak = loopBreak;
        this.id = id;
    }

    @Override
    public String toString() {
        return "loop(id:" + id + (loopPreContinue != null ? ",precontinue:" + loopPreContinue : "") + ",continue:" + loopContinue + ", break:" + loopBreak + ", phase:" + phase + ")";
    }
}
