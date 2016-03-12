/**
 * Copyright (c) 2004-2005, Regents of the University of California
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * Neither the name of the University of California, Los Angeles nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package edu.ucla.cs.compilers.avrora.avrora.arch.legacy;

import edu.ucla.cs.compilers.avrora.avrora.arch.AbstractDisassembler;
import edu.ucla.cs.compilers.avrora.avrora.arch.AbstractInstr;
import edu.ucla.cs.compilers.avrora.cck.text.StringUtil;
import edu.ucla.cs.compilers.avrora.cck.util.Arithmetic;

/**
 * The <code>LegacyDisassembler</code> class is (partially) generated from the
 * instruction set description. It consists a public access method
 * <code>disassemble()</code> that given a byte array of machine code, a base
 * address, and an index, will produce an instance of the
 * <code>LegacyInstr</code> class that corresponds to that machine code
 * instruction. In the case that the machine code does not represent a valid
 * instruction, the method will
 *
 * <p>
 * The decision tree(s) for disassembling machine code into
 * <code>LegacyInstr</code> instances is derived automatically from the ISDL
 * description of the binary encodings of instructions. This decision tree is
 * then translated into Java code in this class. Therefore, most of the code in
 * this class is generated automatically.
 *
 * @author Ben L. Titzer
 */
public class LegacyDisassembler implements AbstractDisassembler
{

    // --BEGIN DISASSEM GENERATOR--
    static final LegacyRegister[] GPR_table = { LegacyRegister.R0,
            LegacyRegister.R1, LegacyRegister.R2, LegacyRegister.R3,
            LegacyRegister.R4, LegacyRegister.R5, LegacyRegister.R6,
            LegacyRegister.R7, LegacyRegister.R8, LegacyRegister.R9,
            LegacyRegister.R10, LegacyRegister.R11, LegacyRegister.R12,
            LegacyRegister.R13, LegacyRegister.R14, LegacyRegister.R15,
            LegacyRegister.R16, LegacyRegister.R17, LegacyRegister.R18,
            LegacyRegister.R19, LegacyRegister.R20, LegacyRegister.R21,
            LegacyRegister.R22, LegacyRegister.R23, LegacyRegister.R24,
            LegacyRegister.R25, LegacyRegister.R26, LegacyRegister.R27,
            LegacyRegister.R28, LegacyRegister.R29, LegacyRegister.R30,
            LegacyRegister.R31 };
    static final LegacyRegister[] HGPR_table = { LegacyRegister.R16,
            LegacyRegister.R17, LegacyRegister.R18, LegacyRegister.R19,
            LegacyRegister.R20, LegacyRegister.R21, LegacyRegister.R22,
            LegacyRegister.R23, LegacyRegister.R24, LegacyRegister.R25,
            LegacyRegister.R26, LegacyRegister.R27, LegacyRegister.R28,
            LegacyRegister.R29, LegacyRegister.R30, LegacyRegister.R31 };
    static final LegacyRegister[] MGPR_table = { LegacyRegister.R16,
            LegacyRegister.R17, LegacyRegister.R18, LegacyRegister.R19,
            LegacyRegister.R20, LegacyRegister.R21, LegacyRegister.R22,
            LegacyRegister.R23 };
    static final LegacyRegister[] YZ_table = { LegacyRegister.Z,
            LegacyRegister.Y };
    static final LegacyRegister[] Z_table = { LegacyRegister.Z };
    static final LegacyRegister[] EGPR_table = { LegacyRegister.R0,
            LegacyRegister.R2, LegacyRegister.R4, LegacyRegister.R6,
            LegacyRegister.R8, LegacyRegister.R10, LegacyRegister.R12,
            LegacyRegister.R14, LegacyRegister.R16, LegacyRegister.R18,
            LegacyRegister.R20, LegacyRegister.R22, LegacyRegister.R24,
            LegacyRegister.R26, LegacyRegister.R28, LegacyRegister.R30 };
    static final LegacyRegister[] RDL_table = { LegacyRegister.R24,
            LegacyRegister.R26, LegacyRegister.R28, LegacyRegister.R30 };
    static final LegacyRegister[] XYZ_table = { LegacyRegister.X,
            LegacyRegister.Y, LegacyRegister.Z, null };
    int pc;
    int index;
    byte[] code;

    /**
     * The <code>disassemble()</code> method disassembles a single instruction from binary code. It accepts a
     * byte array containing the machine code, an index into the array, and the base PC (which is needed for
     * disassembling PC-relative operands).
     *
     * @param base  the base PC address of the beginning of the binary code array
     * @param index an index into the binary code array from which to disassemble an instruction
     * @param code  byte array containing the machine code,
     * @return a new <code>LegacyInstr</code> instance representing the instruction at that address, if the
     * machine code is a valid instruction; null if the instruction is invalid
     */
    @Override
    public AbstractInstr disassemble(int base, int index, byte[] code) {
        return disassembleLegacy(code, base, index);
    }

    public LegacyInstr disassembleLegacy(byte[] code, int base, int index) {
        try {
            int word1 = Arithmetic.word(code[index], code[index + 1]);
            this.index = index;
            this.pc = base + index;
            this.code = code;
            return decode_root(word1);
        } catch (InvalidInstruction e) {
            return null;
        } catch (LegacyInstr.InvalidImmediate e) {
            return null;
        }
    }

    private LegacyRegister getReg(LegacyRegister[] table, int index) throws InvalidInstruction {
        if (index < 0 || index >= table.length) {
            throw new InvalidInstruction(getWord(0), pc);
        }
        LegacyRegister reg = table[index];
        if (reg == null) {
            throw new InvalidInstruction(getWord(0), pc);
        }
        return reg;
    }

    private int getWord(int word) {
        return Arithmetic.word(code[index + word * 2], code[index + word * 2 + 1]);
    }

    // unused
    /*
     * private int getByte(int word) { return code[word*2] & 0xff; }
     */
    private int relative(int address, int signbit) {
        address = Arithmetic.signExtend(address, signbit);
        return address + pc + 1;
    }

    private LegacyInstr decode_BST_0(int word1) throws InvalidInstruction
    {
        if ((word1 & 0x00008) != 0x00000)
        {
            return null;
        }
        int rr = 0;
        int bit = 0;
        // logical[0:6] ->
        // logical[7:11] -> rr[4:0]
        rr |= ((word1 >> 4) & 0x0001F);
        // logical[12:12] ->
        // logical[13:15] -> bit[2:0]
        bit |= (word1 & 0x00007);
        return new LegacyInstr.BST(pc, getReg(GPR_table, rr), bit);
    }

    private LegacyInstr decode_BLD_0(int word1) throws InvalidInstruction {
        if ((word1 & 0x00008) != 0x00000) {
            return null;
        }
        int rr = 0;
        int bit = 0;
        // logical[0:6] ->
        // logical[7:11] -> rr[4:0]
        rr |= ((word1 >> 4) & 0x0001F);
        // logical[12:12] ->
        // logical[13:15] -> bit[2:0]
        bit |= (word1 & 0x00007);
        return new LegacyInstr.BLD(pc, getReg(GPR_table, rr), bit);
    }

    private LegacyInstr decode_0(int word1) throws InvalidInstruction {
        // get value of bits logical[6:6]
        int value = (word1 >> 9) & 0x00001;
        switch (value) {
            case 0x00001:
                return decode_BST_0(word1);
            case 0x00000:
                return decode_BLD_0(word1);
            default:
                return null;
        }
    }

    private LegacyInstr decode_BRPL_0(int word1) throws InvalidInstruction
    {
        int target = 0;
        // logical[0:5] ->
        // logical[6:12] -> target[6:0]
        target |= ((word1 >> 3) & 0x0007F);
        // logical[13:15] ->
        return new LegacyInstr.BRPL(pc, relative(target, 6));
    }

    private LegacyInstr decode_BRGE_0(int word1) throws InvalidInstruction {
        int target = 0;
        // logical[0:5] ->
        // logical[6:12] -> target[6:0]
        target |= ((word1 >> 3) & 0x0007F);
        // logical[13:15] ->
        return new LegacyInstr.BRGE(pc, relative(target, 6));
    }

    private LegacyInstr decode_BRTC_0(int word1) throws InvalidInstruction {
        int target = 0;
        // logical[0:5] ->
        // logical[6:12] -> target[6:0]
        target |= ((word1 >> 3) & 0x0007F);
        // logical[13:15] ->
        return new LegacyInstr.BRTC(pc, relative(target, 6));
    }

    private LegacyInstr decode_BRNE_0(int word1) throws InvalidInstruction {
        int target = 0;
        // logical[0:5] ->
        // logical[6:12] -> target[6:0]
        target |= ((word1 >> 3) & 0x0007F);
        // logical[13:15] ->
        return new LegacyInstr.BRNE(pc, relative(target, 6));
    }

    private LegacyInstr decode_BRVC_0(int word1) throws InvalidInstruction {
        int target = 0;
        // logical[0:5] ->
        // logical[6:12] -> target[6:0]
        target |= ((word1 >> 3) & 0x0007F);
        // logical[13:15] ->
        return new LegacyInstr.BRVC(pc, relative(target, 6));
    }

    private LegacyInstr decode_BRID_0(int word1) throws InvalidInstruction {
        int target = 0;
        // logical[0:5] ->
        // logical[6:12] -> target[6:0]
        target |= ((word1 >> 3) & 0x0007F);
        // logical[13:15] ->
        return new LegacyInstr.BRID(pc, relative(target, 6));
    }

    private LegacyInstr decode_BRHC_0(int word1) throws InvalidInstruction {
        int target = 0;
        // logical[0:5] ->
        // logical[6:12] -> target[6:0]
        target |= ((word1 >> 3) & 0x0007F);
        // logical[13:15] ->
        return new LegacyInstr.BRHC(pc, relative(target, 6));
    }

    private LegacyInstr decode_BRCC_0(int word1) throws InvalidInstruction {
        int target = 0;
        // logical[0:5] ->
        // logical[6:12] -> target[6:0]
        target |= ((word1 >> 3) & 0x0007F);
        // logical[13:15] ->
        return new LegacyInstr.BRCC(pc, relative(target, 6));
    }

    private LegacyInstr decode_1(int word1) throws InvalidInstruction {
        // get value of bits logical[13:15]
        int value = (word1) & 0x00007;
        switch (value) {
            case 0x00002:
                return decode_BRPL_0(word1);
            case 0x00004:
                return decode_BRGE_0(word1);
            case 0x00006:
                return decode_BRTC_0(word1);
            case 0x00001:
                return decode_BRNE_0(word1);
            case 0x00003:
                return decode_BRVC_0(word1);
            case 0x00007:
                return decode_BRID_0(word1);
            case 0x00005:
                return decode_BRHC_0(word1);
            case 0x00000:
                return decode_BRCC_0(word1);
            default:
                return null;
        }
    }

    private LegacyInstr decode_SBRS_0(int word1) throws InvalidInstruction {
        if ((word1 & 0x00008) != 0x00000) {
            return null;
        }
        int rr = 0;
        int bit = 0;
        // logical[0:6] ->
        // logical[7:11] -> rr[4:0]
        rr |= ((word1 >> 4) & 0x0001F);
        // logical[12:12] ->
        // logical[13:15] -> bit[2:0]
        bit |= (word1 & 0x00007);
        return new LegacyInstr.SBRS(pc, getReg(GPR_table, rr), bit);
    }

    private LegacyInstr decode_SBRC_0(int word1) throws InvalidInstruction {
        if ((word1 & 0x00008) != 0x00000) {
            return null;
        }
        int rr = 0;
        int bit = 0;
        // logical[0:6] ->
        // logical[7:11] -> rr[4:0]
        rr |= ((word1 >> 4) & 0x0001F);
        // logical[12:12] ->
        // logical[13:15] -> bit[2:0]
        bit |= (word1 & 0x00007);
        return new LegacyInstr.SBRC(pc, getReg(GPR_table, rr), bit);
    }

    private LegacyInstr decode_2(int word1) throws InvalidInstruction {
        // get value of bits logical[6:6]
        int value = (word1 >> 9) & 0x00001;
        switch (value) {
            case 0x00001:
                return decode_SBRS_0(word1);
            case 0x00000:
                return decode_SBRC_0(word1);
            default:
                return null;
        }
    }

    private LegacyInstr decode_BRMI_0(int word1) throws InvalidInstruction
    {
        int target = 0;
        // logical[0:5] ->
        // logical[6:12] -> target[6:0]
        target |= ((word1 >> 3) & 0x0007F);
        // logical[13:15] ->
        return new LegacyInstr.BRMI(pc, relative(target, 6));
    }

    private LegacyInstr decode_BRLT_0(int word1) throws InvalidInstruction {
        int target = 0;
        // logical[0:5] ->
        // logical[6:12] -> target[6:0]
        target |= ((word1 >> 3) & 0x0007F);
        // logical[13:15] ->
        return new LegacyInstr.BRLT(pc, relative(target, 6));
    }

    private LegacyInstr decode_BRTS_0(int word1) throws InvalidInstruction {
        int target = 0;
        // logical[0:5] ->
        // logical[6:12] -> target[6:0]
        target |= ((word1 >> 3) & 0x0007F);
        // logical[13:15] ->
        return new LegacyInstr.BRTS(pc, relative(target, 6));
    }

    private LegacyInstr decode_BREQ_0(int word1) throws InvalidInstruction {
        int target = 0;
        // logical[0:5] ->
        // logical[6:12] -> target[6:0]
        target |= ((word1 >> 3) & 0x0007F);
        // logical[13:15] ->
        return new LegacyInstr.BREQ(pc, relative(target, 6));
    }

    private LegacyInstr decode_BRVS_0(int word1) throws InvalidInstruction {
        int target = 0;
        // logical[0:5] ->
        // logical[6:12] -> target[6:0]
        target |= ((word1 >> 3) & 0x0007F);
        // logical[13:15] ->
        return new LegacyInstr.BRVS(pc, relative(target, 6));
    }

    private LegacyInstr decode_BRIE_0(int word1) throws InvalidInstruction {
        int target = 0;
        // logical[0:5] ->
        // logical[6:12] -> target[6:0]
        target |= ((word1 >> 3) & 0x0007F);
        // logical[13:15] ->
        return new LegacyInstr.BRIE(pc, relative(target, 6));
    }

    private LegacyInstr decode_BRHS_0(int word1) throws InvalidInstruction {
        int target = 0;
        // logical[0:5] ->
        // logical[6:12] -> target[6:0]
        target |= ((word1 >> 3) & 0x0007F);
        // logical[13:15] ->
        return new LegacyInstr.BRHS(pc, relative(target, 6));
    }

    private LegacyInstr decode_BRCS_0(int word1) throws InvalidInstruction {
        int target = 0;
        // logical[0:5] ->
        // logical[6:12] -> target[6:0]
        target |= ((word1 >> 3) & 0x0007F);
        // logical[13:15] ->
        return new LegacyInstr.BRCS(pc, relative(target, 6));
    }

    private LegacyInstr decode_3(int word1) throws InvalidInstruction {
        // get value of bits logical[13:15]
        int value = (word1) & 0x00007;
        switch (value) {
            case 0x00002:
                return decode_BRMI_0(word1);
            case 0x00004:
                return decode_BRLT_0(word1);
            case 0x00006:
                return decode_BRTS_0(word1);
            case 0x00001:
                return decode_BREQ_0(word1);
            case 0x00003:
                return decode_BRVS_0(word1);
            case 0x00007:
                return decode_BRIE_0(word1);
            case 0x00005:
                return decode_BRHS_0(word1);
            case 0x00000:
                return decode_BRCS_0(word1);
            default:
                return null;
        }
    }

    private LegacyInstr decode_4(int word1) throws InvalidInstruction {
        // get value of bits logical[4:5]
        int value = (word1 >> 10) & 0x00003;
        switch (value) {
            case 0x00002:
                return decode_0(word1);
        case 0x00001:
            return decode_1(word1);
            case 0x00003:
                return decode_2(word1);
            case 0x00000:
                return decode_3(word1);
            default:
                return null;
        }
    }

    private LegacyInstr decode_SBCI_0(int word1) throws InvalidInstruction
    {
        int rd = 0;
        int imm = 0;
        // logical[0:3] ->
        // logical[4:7] -> imm[7:4]
        imm |= ((word1 >> 8) & 0x0000F) << 4;
        // logical[8:11] -> rd[3:0]
        rd |= ((word1 >> 4) & 0x0000F);
        // logical[12:15] -> imm[3:0]
        imm |= (word1 & 0x0000F);
        return new LegacyInstr.SBCI(pc, getReg(HGPR_table, rd), imm);
    }

    private LegacyInstr decode_ST_1(int word1) throws InvalidInstruction {
        // this method decodes ST when ar == Y
        int rr = 0;
        // logical[0:6] ->
        // logical[7:11] -> rr[4:0]
        rr |= ((word1 >> 4) & 0x0001F);
        // logical[12:15] ->
        return new LegacyInstr.ST(pc, LegacyRegister.Y, getReg(GPR_table, rr));
    }

    private LegacyInstr decode_ST_2(int word1) throws InvalidInstruction {
        // this method decodes ST when ar == Z
        int rr = 0;
        // logical[0:6] ->
        // logical[7:11] -> rr[4:0]
        rr |= ((word1 >> 4) & 0x0001F);
        // logical[12:15] ->
        return new LegacyInstr.ST(pc, LegacyRegister.Z, getReg(GPR_table, rr));
    }

    private LegacyInstr decode_5(int word1) throws InvalidInstruction {
        // get value of bits logical[12:15]
        int value = (word1) & 0x0000F;
        switch (value) {
            case 0x00008:
                return decode_ST_1(word1);
            case 0x00000:
                return decode_ST_2(word1);
            default:
                return null;
        }
    }

    private LegacyInstr decode_LD_1(int word1) throws InvalidInstruction {
        // this method decodes LD when ar == Y
        int rd = 0;
        // logical[0:6] ->
        // logical[7:11] -> rd[4:0]
        rd |= ((word1 >> 4) & 0x0001F);
        // logical[12:15] ->
        return new LegacyInstr.LD(pc, getReg(GPR_table, rd), LegacyRegister.Y);
    }

    private LegacyInstr decode_LD_2(int word1) throws InvalidInstruction {
        // this method decodes LD when ar == Z
        int rd = 0;
        // logical[0:6] ->
        // logical[7:11] -> rd[4:0]
        rd |= ((word1 >> 4) & 0x0001F);
        // logical[12:15] ->
        return new LegacyInstr.LD(pc, getReg(GPR_table, rd), LegacyRegister.Z);
    }

    private LegacyInstr decode_6(int word1) throws InvalidInstruction {
        // get value of bits logical[12:15]
        int value = (word1) & 0x0000F;
        switch (value) {
            case 0x00008:
                return decode_LD_1(word1);
            case 0x00000:
                return decode_LD_2(word1);
            default:
                return null;
        }
    }

    private LegacyInstr decode_7(int word1) throws InvalidInstruction {
        // get value of bits logical[4:6]
        int value = (word1 >> 9) & 0x00007;
        switch (value) {
            case 0x00001:
                return decode_5(word1);
        case 0x00000:
            return decode_6(word1);
            default:
                return null;
        }
    }

    private LegacyInstr decode_OUT_0(int word1) throws InvalidInstruction
    {
        int ior = 0;
        int rr = 0;
        // logical[0:4] ->
        // logical[5:6] -> ior[5:4]
        ior |= ((word1 >> 9) & 0x00003) << 4;
        // logical[7:11] -> rr[4:0]
        rr |= ((word1 >> 4) & 0x0001F);
        // logical[12:15] -> ior[3:0]
        ior |= (word1 & 0x0000F);
        return new LegacyInstr.OUT(pc, ior, getReg(GPR_table, rr));
    }

    private LegacyInstr decode_IN_0(int word1) throws InvalidInstruction {
        int rd = 0;
        int imm = 0;
        // logical[0:4] ->
        // logical[5:6] -> imm[5:4]
        imm |= ((word1 >> 9) & 0x00003) << 4;
        // logical[7:11] -> rd[4:0]
        rd |= ((word1 >> 4) & 0x0001F);
        // logical[12:15] -> imm[3:0]
        imm |= (word1 & 0x0000F);
        return new LegacyInstr.IN(pc, getReg(GPR_table, rd), imm);
    }

    private LegacyInstr decode_8(int word1) throws InvalidInstruction {
        // get value of bits logical[4:4]
        int value = (word1 >> 11) & 0x00001;
        switch (value) {
            case 0x00001:
                return decode_OUT_0(word1);
            case 0x00000:
                return decode_IN_0(word1);
            default:
                return null;
        }
    }

    private LegacyInstr decode_CPI_0(int word1) throws InvalidInstruction {
        int rd = 0;
        int imm = 0;
        // logical[0:3] ->
        // logical[4:7] -> imm[7:4]
        imm |= ((word1 >> 8) & 0x0000F) << 4;
        // logical[8:11] -> rd[3:0]
        rd |= ((word1 >> 4) & 0x0000F);
        // logical[12:15] -> imm[3:0]
        imm |= (word1 & 0x0000F);
        return new LegacyInstr.CPI(pc, getReg(HGPR_table, rd), imm);
    }

    private LegacyInstr decode_ANDI_0(int word1) throws InvalidInstruction {
        int rd = 0;
        int imm = 0;
        // logical[0:3] ->
        // logical[4:7] -> imm[7:4]
        imm |= ((word1 >> 8) & 0x0000F) << 4;
        // logical[8:11] -> rd[3:0]
        rd |= ((word1 >> 4) & 0x0000F);
        // logical[12:15] -> imm[3:0]
        imm |= (word1 & 0x0000F);
        return new LegacyInstr.ANDI(pc, getReg(HGPR_table, rd), imm);
    }

    private LegacyInstr decode_RJMP_0(int word1) throws InvalidInstruction {
        int target = 0;
        // logical[0:3] ->
        // logical[4:15] -> target[11:0]
        target |= (word1 & 0x00FFF);
        return new LegacyInstr.RJMP(pc, relative(target, 11));
    }

    private LegacyInstr decode_OR_0(int word1) throws InvalidInstruction {
        int rd = 0;
        int rr = 0;
        // logical[0:5] ->
        // logical[6:6] -> rr[4:4]
        rr |= ((word1 >> 9) & 0x00001) << 4;
        // logical[7:7] -> rd[4:4]
        rd |= ((word1 >> 8) & 0x00001) << 4;
        // logical[8:11] -> rd[3:0]
        rd |= ((word1 >> 4) & 0x0000F);
        // logical[12:15] -> rr[3:0]
        rr |= (word1 & 0x0000F);
        return new LegacyInstr.OR(pc, getReg(GPR_table, rd), getReg(GPR_table, rr));
    }

    private LegacyInstr decode_EOR_0(int word1) throws InvalidInstruction {
        int rd = 0;
        int rr = 0;
        // logical[0:5] ->
        // logical[6:6] -> rr[4:4]
        rr |= ((word1 >> 9) & 0x00001) << 4;
        // logical[7:7] -> rd[4:4]
        rd |= ((word1 >> 8) & 0x00001) << 4;
        // logical[8:11] -> rd[3:0]
        rd |= ((word1 >> 4) & 0x0000F);
        // logical[12:15] -> rr[3:0]
        rr |= (word1 & 0x0000F);
        return new LegacyInstr.EOR(pc, getReg(GPR_table, rd),
                getReg(GPR_table, rr));
    }

    private LegacyInstr decode_MOV_0(int word1) throws InvalidInstruction
    {
        int rd = 0;
        int rr = 0;
        // logical[0:5] ->
        // logical[6:6] -> rr[4:4]
        rr |= ((word1 >> 9) & 0x00001) << 4;
        // logical[7:7] -> rd[4:4]
        rd |= ((word1 >> 8) & 0x00001) << 4;
        // logical[8:11] -> rd[3:0]
        rd |= ((word1 >> 4) & 0x0000F);
        // logical[12:15] -> rr[3:0]
        rr |= (word1 & 0x0000F);
        return new LegacyInstr.MOV(pc, getReg(GPR_table, rd),
                getReg(GPR_table, rr));
    }

    private LegacyInstr decode_AND_0(int word1) throws InvalidInstruction
    {
        int rd = 0;
        int rr = 0;
        // logical[0:5] ->
        // logical[6:6] -> rr[4:4]
        rr |= ((word1 >> 9) & 0x00001) << 4;
        // logical[7:7] -> rd[4:4]
        rd |= ((word1 >> 8) & 0x00001) << 4;
        // logical[8:11] -> rd[3:0]
        rd |= ((word1 >> 4) & 0x0000F);
        // logical[12:15] -> rr[3:0]
        rr |= (word1 & 0x0000F);
        return new LegacyInstr.AND(pc, getReg(GPR_table, rd),
                getReg(GPR_table, rr));
    }

    private LegacyInstr decode_9(int word1) throws InvalidInstruction
    {
        // get value of bits logical[4:5]
        int value = (word1 >> 10) & 0x00003;
        switch (value) {
            case 0x00002:
                return decode_OR_0(word1);
            case 0x00001:
                return decode_EOR_0(word1);
            case 0x00003:
                return decode_MOV_0(word1);
            case 0x00000:
                return decode_AND_0(word1);
            default:
                return null;
        }
    }

    private LegacyInstr decode_RCALL_0(int word1) throws InvalidInstruction
    {
        int target = 0;
        // logical[0:3] ->
        // logical[4:15] -> target[11:0]
        target |= (word1 & 0x00FFF);
        return new LegacyInstr.RCALL(pc, relative(target, 11));
    }

    private LegacyInstr decode_SBI_0(int word1) throws InvalidInstruction {
        int ior = 0;
        int bit = 0;
        // logical[0:7] ->
        // logical[8:12] -> ior[4:0]
        ior |= ((word1 >> 3) & 0x0001F);
        // logical[13:15] -> bit[2:0]
        bit |= (word1 & 0x00007);
        return new LegacyInstr.SBI(pc, ior, bit);
    }

    private LegacyInstr decode_SBIC_0(int word1) throws InvalidInstruction {
        int ior = 0;
        int bit = 0;
        // logical[0:7] ->
        // logical[8:12] -> ior[4:0]
        ior |= ((word1 >> 3) & 0x0001F);
        // logical[13:15] -> bit[2:0]
        bit |= (word1 & 0x00007);
        return new LegacyInstr.SBIC(pc, ior, bit);
    }

    private LegacyInstr decode_SBIS_0(int word1) throws InvalidInstruction {
        int ior = 0;
        int bit = 0;
        // logical[0:7] ->
        // logical[8:12] -> ior[4:0]
        ior |= ((word1 >> 3) & 0x0001F);
        // logical[13:15] -> bit[2:0]
        bit |= (word1 & 0x00007);
        return new LegacyInstr.SBIS(pc, ior, bit);
    }

    private LegacyInstr decode_CBI_0(int word1) throws InvalidInstruction {
        int ior = 0;
        int bit = 0;
        // logical[0:7] ->
        // logical[8:12] -> ior[4:0]
        ior |= ((word1 >> 3) & 0x0001F);
        // logical[13:15] -> bit[2:0]
        bit |= (word1 & 0x00007);
        return new LegacyInstr.CBI(pc, ior, bit);
    }

    private LegacyInstr decode_10(int word1) throws InvalidInstruction {
        // get value of bits logical[6:7]
        int value = (word1 >> 8) & 0x00003;
        switch (value) {
            case 0x00002:
                return decode_SBI_0(word1);
            case 0x00001:
                return decode_SBIC_0(word1);
            case 0x00003:
                return decode_SBIS_0(word1);
            case 0x00000:
                return decode_CBI_0(word1);
            default:
                return null;
        }
    }

    private LegacyInstr decode_SBIW_0(int word1) throws InvalidInstruction {
        int rd = 0;
        int imm = 0;
        // logical[0:7] ->
        // logical[8:9] -> imm[5:4]
        imm |= ((word1 >> 6) & 0x00003) << 4;
        // logical[10:11] -> rd[1:0]
        rd |= ((word1 >> 4) & 0x00003);
        // logical[12:15] -> imm[3:0]
        imm |= (word1 & 0x0000F);
        return new LegacyInstr.SBIW(pc, getReg(RDL_table, rd), imm);
    }

    private LegacyInstr decode_ADIW_0(int word1) throws InvalidInstruction {
        int rd = 0;
        int imm = 0;
        // logical[0:7] ->
        // logical[8:9] -> imm[5:4]
        imm |= ((word1 >> 6) & 0x00003) << 4;
        // logical[10:11] -> rd[1:0]
        rd |= ((word1 >> 4) & 0x00003);
        // logical[12:15] -> imm[3:0]
        imm |= (word1 & 0x0000F);
        return new LegacyInstr.ADIW(pc, getReg(RDL_table, rd), imm);
    }

    private LegacyInstr decode_11(int word1) throws InvalidInstruction {
        // get value of bits logical[7:7]
        int value = (word1 >> 8) & 0x00001;
        switch (value) {
            case 0x00001:
                return decode_SBIW_0(word1);
            case 0x00000:
                return decode_ADIW_0(word1);
            default:
                return null;
        }
    }

    private LegacyInstr decode_ASR_0(int word1) throws InvalidInstruction {
        if ((word1 & 0x00001) != 0x00001) {
            return null;
        }
        int rd = 0;
        // logical[0:6] ->
        // logical[7:7] -> rd[4:4]
        rd |= ((word1 >> 8) & 0x00001) << 4;
        // logical[8:11] -> rd[3:0]
        rd |= ((word1 >> 4) & 0x0000F);
        // logical[12:15] ->
        return new LegacyInstr.ASR(pc, getReg(GPR_table, rd));
    }

    private LegacyInstr decode_CLI_0(int word1) throws InvalidInstruction {
        if ((word1 & 0x00001) != 0x00000) {
            return null;
        }
        // logical[0:7] ->
        // logical[8:8] ->
        // logical[9:11] ->
        // logical[12:15] ->
        return new LegacyInstr.CLI(pc);
    }

    private LegacyInstr decode_SES_0(int word1) throws InvalidInstruction {
        if ((word1 & 0x00001) != 0x00000) {
            return null;
        }
        // logical[0:7] ->
        // logical[8:8] ->
        // logical[9:11] ->
        // logical[12:15] ->
        return new LegacyInstr.SES(pc);
    }

    private LegacyInstr decode_SPM_0(int word1) throws InvalidInstruction {
        if ((word1 & 0x00001) != 0x00000) {
            return null;
        }
        // logical[0:7] ->
        // logical[8:15] ->
        return new LegacyInstr.SPM(pc);
    }

    private LegacyInstr decode_CLC_0(int word1) throws InvalidInstruction {
        if ((word1 & 0x00001) != 0x00000) {
            return null;
        }
        // logical[0:7] ->
        // logical[8:8] ->
        // logical[9:11] ->
        // logical[12:15] ->
        return new LegacyInstr.CLC(pc);
    }

    private LegacyInstr decode_WDR_0(int word1) throws InvalidInstruction {
        if ((word1 & 0x00001) != 0x00000) {
            return null;
        }
        // logical[0:7] ->
        // logical[8:15] ->
        return new LegacyInstr.WDR(pc);
    }

    private LegacyInstr decode_CLV_0(int word1) throws InvalidInstruction {
        if ((word1 & 0x00001) != 0x00000) {
            return null;
        }
        // logical[0:7] ->
        // logical[8:8] ->
        // logical[9:11] ->
        // logical[12:15] ->
        return new LegacyInstr.CLV(pc);
    }

    private LegacyInstr decode_ICALL_0(int word1) throws InvalidInstruction
    {
        // logical[0:7] ->
        // logical[8:15] ->
        return new LegacyInstr.ICALL(pc);
    }

    private LegacyInstr decode_RET_0(int word1) throws InvalidInstruction {
        // logical[0:7] ->
        // logical[8:15] ->
        return new LegacyInstr.RET(pc);
    }

    private LegacyInstr decode_12(int word1) throws InvalidInstruction {
        // get value of bits logical[15:15]
        int value = (word1) & 0x00001;
        switch (value) {
            case 0x00001:
                return decode_ICALL_0(word1);
            case 0x00000:
                return decode_RET_0(word1);
            default:
                return null;
        }
    }

    private LegacyInstr decode_SEV_0(int word1) throws InvalidInstruction {
        if ((word1 & 0x00001) != 0x00000) {
            return null;
        }
        // logical[0:7] ->
        // logical[8:8] ->
        // logical[9:11] ->
        // logical[12:15] ->
        return new LegacyInstr.SEV(pc);
    }

    private LegacyInstr decode_SEI_0(int word1) throws InvalidInstruction {
        if ((word1 & 0x00001) != 0x00000) {
            return null;
        }
        // logical[0:7] ->
        // logical[8:8] ->
        // logical[9:11] ->
        // logical[12:15] ->
        return new LegacyInstr.SEI(pc);
    }

    private LegacyInstr decode_CLS_0(int word1) throws InvalidInstruction {
        if ((word1 & 0x00001) != 0x00000) {
            return null;
        }
        // logical[0:7] ->
        // logical[8:8] ->
        // logical[9:11] ->
        // logical[12:15] ->
        return new LegacyInstr.CLS(pc);
    }

    private LegacyInstr decode_EICALL_0(int word1) throws InvalidInstruction
    {
        // logical[0:7] ->
        // logical[8:15] ->
        return new LegacyInstr.EICALL(pc);
    }

    private LegacyInstr decode_RETI_0(int word1) throws InvalidInstruction {
        // logical[0:7] ->
        // logical[8:15] ->
        return new LegacyInstr.RETI(pc);
    }

    private LegacyInstr decode_13(int word1) throws InvalidInstruction {
        // get value of bits logical[15:15]
        int value = (word1) & 0x00001;
        switch (value) {
            case 0x00001:
                return decode_EICALL_0(word1);
            case 0x00000:
                return decode_RETI_0(word1);
            default:
                return null;
        }
    }

    private LegacyInstr decode_SEN_0(int word1) throws InvalidInstruction {
        if ((word1 & 0x00001) != 0x00000) {
            return null;
        }
        // logical[0:7] ->
        // logical[8:8] ->
        // logical[9:11] ->
        // logical[12:15] ->
        return new LegacyInstr.SEN(pc);
    }

    private LegacyInstr decode_CLH_0(int word1) throws InvalidInstruction {
        if ((word1 & 0x00001) != 0x00000) {
            return null;
        }
        // logical[0:7] ->
        // logical[8:8] ->
        // logical[9:11] ->
        // logical[12:15] ->
        return new LegacyInstr.CLH(pc);
    }

    private LegacyInstr decode_CLZ_0(int word1) throws InvalidInstruction {
        if ((word1 & 0x00001) != 0x00000) {
            return null;
        }
        // logical[0:7] ->
        // logical[8:8] ->
        // logical[9:11] ->
        // logical[12:15] ->
        return new LegacyInstr.CLZ(pc);
    }

    private LegacyInstr decode_LPM_0(int word1) throws InvalidInstruction {
        if ((word1 & 0x00001) != 0x00000) {
            return null;
        }
        // logical[0:7] ->
        // logical[8:15] ->
        return new LegacyInstr.LPM(pc);
    }

    private LegacyInstr decode_SET_0(int word1) throws InvalidInstruction {
        if ((word1 & 0x00001) != 0x00000) {
            return null;
        }
        // logical[0:7] ->
        // logical[8:8] ->
        // logical[9:11] ->
        // logical[12:15] ->
        return new LegacyInstr.SET(pc);
    }

    private LegacyInstr decode_EIJMP_0(int word1) throws InvalidInstruction
    {
        // logical[0:7] ->
        // logical[8:15] ->
        return new LegacyInstr.EIJMP(pc);
    }

    private LegacyInstr decode_SEZ_0(int word1) throws InvalidInstruction {
        // logical[0:7] ->
        // logical[8:8] ->
        // logical[9:11] ->
        // logical[12:15] ->
        return new LegacyInstr.SEZ(pc);
    }

    private LegacyInstr decode_14(int word1) throws InvalidInstruction {
        // get value of bits logical[15:15]
        int value = (word1) & 0x00001;
        switch (value) {
            case 0x00001:
                return decode_EIJMP_0(word1);
            case 0x00000:
                return decode_SEZ_0(word1);
            default:
                return null;
        }
    }

    private LegacyInstr decode_ELPM_0(int word1) throws InvalidInstruction {
        if ((word1 & 0x00001) != 0x00000) {
            return null;
        }
        // logical[0:7] ->
        // logical[8:15] ->
        return new LegacyInstr.ELPM(pc);
    }

    private LegacyInstr decode_CLT_0(int word1) throws InvalidInstruction {
        if ((word1 & 0x00001) != 0x00000) {
            return null;
        }
        // logical[0:7] ->
        // logical[8:8] ->
        // logical[9:11] ->
        // logical[12:15] ->
        return new LegacyInstr.CLT(pc);
    }

    private LegacyInstr decode_BREAK_0(int word1) throws InvalidInstruction
    {
        if ((word1 & 0x00001) != 0x00000) {
            return null;
        }
        // logical[0:7] ->
        // logical[8:15] ->
        return new LegacyInstr.BREAK(pc);
    }

    private LegacyInstr decode_SLEEP_0(int word1) throws InvalidInstruction
    {
        if ((word1 & 0x00001) != 0x00000) {
            return null;
        }
        // logical[0:7] ->
        // logical[8:15] ->
        return new LegacyInstr.SLEEP(pc);
    }

    private LegacyInstr decode_CLN_0(int word1) throws InvalidInstruction {
        if ((word1 & 0x00001) != 0x00000) {
            return null;
        }
        // logical[0:7] ->
        // logical[8:8] ->
        // logical[9:11] ->
        // logical[12:15] ->
        return new LegacyInstr.CLN(pc);
    }

    private LegacyInstr decode_SEH_0(int word1) throws InvalidInstruction {
        if ((word1 & 0x00001) != 0x00000) {
            return null;
        }
        // logical[0:7] ->
        // logical[8:8] ->
        // logical[9:11] ->
        // logical[12:15] ->
        return new LegacyInstr.SEH(pc);
    }

    private LegacyInstr decode_IJMP_0(int word1) throws InvalidInstruction {
        // logical[0:7] ->
        // logical[8:15] ->
        return new LegacyInstr.IJMP(pc);
    }

    private LegacyInstr decode_SEC_0(int word1) throws InvalidInstruction {
        // logical[0:7] ->
        // logical[8:8] ->
        // logical[9:11] ->
        // logical[12:15] ->
        return new LegacyInstr.SEC(pc);
    }

    private LegacyInstr decode_15(int word1) throws InvalidInstruction {
        // get value of bits logical[15:15]
        int value = (word1) & 0x00001;
        switch (value) {
            case 0x00001:
                return decode_IJMP_0(word1);
            case 0x00000:
                return decode_SEC_0(word1);
            default:
                return null;
        }
    }

    private LegacyInstr decode_16(int word1) throws InvalidInstruction {
        // get value of bits logical[7:11]
        int value = (word1 >> 4) & 0x0001F;
        switch (value) {
            case 0x0000F:
                return decode_CLI_0(word1);
        case 0x00004:
            return decode_SES_0(word1);
            case 0x0001E:
                return decode_SPM_0(word1);
            case 0x00008:
                return decode_CLC_0(word1);
            case 0x0001A:
                return decode_WDR_0(word1);
            case 0x0000B:
                return decode_CLV_0(word1);
            case 0x00010:
                return decode_12(word1);
            case 0x00003:
                return decode_SEV_0(word1);
            case 0x00007:
                return decode_SEI_0(word1);
            case 0x0000C:
                return decode_CLS_0(word1);
            case 0x00011:
                return decode_13(word1);
            case 0x00002:
                return decode_SEN_0(word1);
            case 0x0000D:
                return decode_CLH_0(word1);
            case 0x00009:
                return decode_CLZ_0(word1);
            case 0x0001C:
                return decode_LPM_0(word1);
            case 0x00006:
                return decode_SET_0(word1);
            case 0x00001:
                return decode_14(word1);
            case 0x0001D:
                return decode_ELPM_0(word1);
            case 0x0000E:
                return decode_CLT_0(word1);
            case 0x00019:
                return decode_BREAK_0(word1);
            case 0x00018:
                return decode_SLEEP_0(word1);
            case 0x0000A:
                return decode_CLN_0(word1);
            case 0x00005:
                return decode_SEH_0(word1);
            case 0x00000:
                return decode_15(word1);
            default:
                return null;
        }
    }

    private LegacyInstr decode_JMP_0(int word1) throws InvalidInstruction
    {
        int word2 = getWord(1);
        int target = 0;
        // logical[0:6] ->
        // logical[7:11] -> target[21:17]
        target |= ((word1 >> 4) & 0x0001F) << 17;
        // logical[12:14] ->
        // logical[15:15] -> target[16:16]
        target |= (word1 & 0x00001) << 16;
        // logical[16:31] -> target[15:0]
        target |= (word2 & 0x0FFFF);
        return new LegacyInstr.JMP(pc, target);
    }

    private LegacyInstr decode_INC_0(int word1) throws InvalidInstruction {
        int rd = 0;
        // logical[0:6] ->
        // logical[7:11] -> rd[4:0]
        rd |= ((word1 >> 4) & 0x0001F);
        // logical[12:15] ->
        return new LegacyInstr.INC(pc, getReg(GPR_table, rd));
    }

    private LegacyInstr decode_SWAP_0(int word1) throws InvalidInstruction {
        int rd = 0;
        // logical[0:6] ->
        // logical[7:11] -> rd[4:0]
        rd |= ((word1 >> 4) & 0x0001F);
        // logical[12:15] ->
        return new LegacyInstr.SWAP(pc, getReg(GPR_table, rd));
    }

    private LegacyInstr decode_17(int word1) throws InvalidInstruction {
        // get value of bits logical[15:15]
        int value = (word1) & 0x00001;
        switch (value) {
            case 0x00001:
                return decode_INC_0(word1);
            case 0x00000:
                return decode_SWAP_0(word1);
            default:
                return null;
        }
    }

    private LegacyInstr decode_ROR_0(int word1) throws InvalidInstruction {
        int rd = 0;
        // logical[0:6] ->
        // logical[7:11] -> rd[4:0]
        rd |= ((word1 >> 4) & 0x0001F);
        // logical[12:15] ->
        return new LegacyInstr.ROR(pc, getReg(GPR_table, rd));
    }

    private LegacyInstr decode_LSR_0(int word1) throws InvalidInstruction {
        int rd = 0;
        // logical[0:6] ->
        // logical[7:11] -> rd[4:0]
        rd |= ((word1 >> 4) & 0x0001F);
        // logical[12:15] ->
        return new LegacyInstr.LSR(pc, getReg(GPR_table, rd));
    }

    private LegacyInstr decode_18(int word1) throws InvalidInstruction {
        // get value of bits logical[15:15]
        int value = (word1) & 0x00001;
        switch (value) {
            case 0x00001:
                return decode_ROR_0(word1);
            case 0x00000:
                return decode_LSR_0(word1);
            default:
                return null;
        }
    }

    private LegacyInstr decode_CALL_0(int word1) throws InvalidInstruction {
        int word2 = getWord(1);
        int target = 0;
        // logical[0:6] ->
        // logical[7:11] -> target[21:17]
        target |= ((word1 >> 4) & 0x0001F) << 17;
        // logical[12:14] ->
        // logical[15:15] -> target[16:16]
        target |= (word1 & 0x00001) << 16;
        // logical[16:31] -> target[15:0]
        target |= (word2 & 0x0FFFF);
        return new LegacyInstr.CALL(pc, target);
    }

    private LegacyInstr decode_DEC_0(int word1) throws InvalidInstruction {
        if ((word1 & 0x00001) != 0x00000) {
            return null;
        }
        int rd = 0;
        // logical[0:6] ->
        // logical[7:11] -> rd[4:0]
        rd |= ((word1 >> 4) & 0x0001F);
        // logical[12:15] ->
        return new LegacyInstr.DEC(pc, getReg(GPR_table, rd));
    }

    private LegacyInstr decode_NEG_0(int word1) throws InvalidInstruction {
        int rd = 0;
        // logical[0:6] ->
        // logical[7:11] -> rd[4:0]
        rd |= ((word1 >> 4) & 0x0001F);
        // logical[12:15] ->
        return new LegacyInstr.NEG(pc, getReg(GPR_table, rd));
    }

    private LegacyInstr decode_COM_0(int word1) throws InvalidInstruction {
        int rd = 0;
        // logical[0:6] ->
        // logical[7:11] -> rd[4:0]
        rd |= ((word1 >> 4) & 0x0001F);
        // logical[12:15] ->
        return new LegacyInstr.COM(pc, getReg(GPR_table, rd));
    }

    private LegacyInstr decode_19(int word1) throws InvalidInstruction {
        // get value of bits logical[15:15]
        int value = (word1) & 0x00001;
        switch (value) {
            case 0x00001:
                return decode_NEG_0(word1);
            case 0x00000:
                return decode_COM_0(word1);
            default:
                return null;
        }
    }

    private LegacyInstr decode_20(int word1) throws InvalidInstruction {
        // get value of bits logical[12:14]
        int value = (word1 >> 1) & 0x00007;
        switch (value) {
            case 0x00002:
                return decode_ASR_0(word1);
        case 0x00004:
            return decode_16(word1);
            case 0x00006:
                return decode_JMP_0(word1);
            case 0x00001:
                return decode_17(word1);
            case 0x00003:
                return decode_18(word1);
            case 0x00007:
                return decode_CALL_0(word1);
            case 0x00005:
                return decode_DEC_0(word1);
            case 0x00000:
                return decode_19(word1);
            default:
                return null;
        }
    }

    private LegacyInstr decode_21(int word1) throws InvalidInstruction
    {
        // get value of bits logical[6:6]
        int value = (word1 >> 9) & 0x00001;
        switch (value) {
            case 0x00001:
                return decode_11(word1);
        case 0x00000:
            return decode_20(word1);
            default:
                return null;
        }
    }

    private LegacyInstr decode_MUL_0(int word1) throws InvalidInstruction
    {
        int rd = 0;
        int rr = 0;
        // logical[0:5] ->
        // logical[6:6] -> rr[4:4]
        rr |= ((word1 >> 9) & 0x00001) << 4;
        // logical[7:7] -> rd[4:4]
        rd |= ((word1 >> 8) & 0x00001) << 4;
        // logical[8:11] -> rd[3:0]
        rd |= ((word1 >> 4) & 0x0000F);
        // logical[12:15] -> rr[3:0]
        rr |= (word1 & 0x0000F);
        return new LegacyInstr.MUL(pc, getReg(GPR_table, rd), getReg(GPR_table, rr));
    }

    private LegacyInstr decode_STPD_2(int word1) throws InvalidInstruction {
        // this method decodes STPD when ar == Z
        int rr = 0;
        // logical[0:6] ->
        // logical[7:11] -> rr[4:0]
        rr |= ((word1 >> 4) & 0x0001F);
        // logical[12:15] ->
        return new LegacyInstr.STPD(pc, LegacyRegister.Z, getReg(GPR_table, rr));
    }

    private LegacyInstr decode_PUSH_0(int word1) throws InvalidInstruction
    {
        int rr = 0;
        // logical[0:6] ->
        // logical[7:11] -> rr[4:0]
        rr |= ((word1 >> 4) & 0x0001F);
        // logical[12:15] ->
        return new LegacyInstr.PUSH(pc, getReg(GPR_table, rr));
    }

    private LegacyInstr decode_STPI_0(int word1) throws InvalidInstruction {
        // this method decodes STPI when ar == X
        int rr = 0;
        // logical[0:6] ->
        // logical[7:11] -> rr[4:0]
        rr |= ((word1 >> 4) & 0x0001F);
        // logical[12:15] ->
        return new LegacyInstr.STPI(pc, LegacyRegister.X,
                getReg(GPR_table, rr));
    }

    private LegacyInstr decode_STPI_1(int word1) throws InvalidInstruction {
        // this method decodes STPI when ar == Y
        int rr = 0;
        // logical[0:6] ->
        // logical[7:11] -> rr[4:0]
        rr |= ((word1 >> 4) & 0x0001F);
        // logical[12:15] ->
        return new LegacyInstr.STPI(pc, LegacyRegister.Y,
                getReg(GPR_table, rr));
    }

    private LegacyInstr decode_STPI_2(int word1) throws InvalidInstruction
    {
        // this method decodes STPI when ar == Z
        int rr = 0;
        // logical[0:6] ->
        // logical[7:11] -> rr[4:0]
        rr |= ((word1 >> 4) & 0x0001F);
        // logical[12:15] ->
        return new LegacyInstr.STPI(pc, LegacyRegister.Z,
                getReg(GPR_table, rr));
    }

    private LegacyInstr decode_STPD_0(int word1) throws InvalidInstruction
    {
        // this method decodes STPD when ar == X
        int rr = 0;
        // logical[0:6] ->
        // logical[7:11] -> rr[4:0]
        rr |= ((word1 >> 4) & 0x0001F);
        // logical[12:15] ->
        return new LegacyInstr.STPD(pc, LegacyRegister.X,
                getReg(GPR_table, rr));
    }

    private LegacyInstr decode_STPD_1(int word1) throws InvalidInstruction
    {
        // this method decodes STPD when ar == Y
        int rr = 0;
        // logical[0:6] ->
        // logical[7:11] -> rr[4:0]
        rr |= ((word1 >> 4) & 0x0001F);
        // logical[12:15] ->
        return new LegacyInstr.STPD(pc, LegacyRegister.Y,
                getReg(GPR_table, rr));
    }

    private LegacyInstr decode_ST_0(int word1) throws InvalidInstruction
    {
        // this method decodes ST when ar == X
        int rr = 0;
        // logical[0:6] ->
        // logical[7:11] -> rr[4:0]
        rr |= ((word1 >> 4) & 0x0001F);
        // logical[12:15] ->
        return new LegacyInstr.ST(pc, LegacyRegister.X, getReg(GPR_table, rr));
    }

    private LegacyInstr decode_STS_0(int word1) throws InvalidInstruction {
        int word2 = getWord(1);
        int addr = 0;
        int rr = 0;
        // logical[0:6] ->
        // logical[7:11] -> rr[4:0]
        rr |= ((word1 >> 4) & 0x0001F);
        // logical[12:15] ->
        // logical[16:31] -> addr[15:0]
        addr |= (word2 & 0x0FFFF);
        return new LegacyInstr.STS(pc, addr, getReg(GPR_table, rr));
    }

    private LegacyInstr decode_22(int word1) throws InvalidInstruction {
        // get value of bits logical[12:15]
        int value = (word1) & 0x0000F;
        switch (value) {
            case 0x00002:
                return decode_STPD_2(word1);
            case 0x0000F:
                return decode_PUSH_0(word1);
            case 0x0000D:
                return decode_STPI_0(word1);
            case 0x00009:
                return decode_STPI_1(word1);
            case 0x00001:
                return decode_STPI_2(word1);
            case 0x0000E:
                return decode_STPD_0(word1);
            case 0x0000A:
                return decode_STPD_1(word1);
            case 0x0000C:
                return decode_ST_0(word1);
            case 0x00000:
                return decode_STS_0(word1);
            default:
                return null;
        }
    }

    private LegacyInstr decode_POP_0(int word1) throws InvalidInstruction {
        int rd = 0;
        // logical[0:6] ->
        // logical[7:11] -> rd[4:0]
        rd |= ((word1 >> 4) & 0x0001F);
        // logical[12:15] ->
        return new LegacyInstr.POP(pc, getReg(GPR_table, rd));
    }

    private LegacyInstr decode_LPMD_0(int word1) throws InvalidInstruction {
        int rd = 0;
        int z = 0;
        // logical[0:6] ->
        // logical[7:11] -> rd[4:0]
        rd |= ((word1 >> 4) & 0x0001F);
        // logical[12:15] ->
        return new LegacyInstr.LPMD(pc, getReg(GPR_table, rd),
                getReg(Z_table, z));
    }

    private LegacyInstr decode_ELPMPI_0(int word1) throws InvalidInstruction
    {
        int rd = 0;
        int rr = 0;
        // logical[0:6] ->
        // logical[7:11] -> rd[4:0]
        rd |= ((word1 >> 4) & 0x0001F);
        // logical[12:15] ->
        return new LegacyInstr.ELPMPI(pc, getReg(GPR_table, rd),
                getReg(Z_table, rr));
    }

    private LegacyInstr decode_LD_0(int word1) throws InvalidInstruction {
        // this method decodes LD when ar == X
        int rd = 0;
        // logical[0:6] ->
        // logical[7:11] -> rd[4:0]
        rd |= ((word1 >> 4) & 0x0001F);
        // logical[12:15] ->
        return new LegacyInstr.LD(pc, getReg(GPR_table, rd), LegacyRegister.X);
    }

    private LegacyInstr decode_LDPD_2(int word1) throws InvalidInstruction {
        // this method decodes LDPD when ar == Z
        int rd = 0;
        // logical[0:6] ->
        // logical[7:11] -> rd[4:0]
        rd |= ((word1 >> 4) & 0x0001F);
        // logical[12:15] ->
        return new LegacyInstr.LDPD(pc, getReg(GPR_table, rd),
                LegacyRegister.Z);
    }

    private LegacyInstr decode_LDPI_0(int word1) throws InvalidInstruction {
        // this method decodes LDPI when ar == X
        int rd = 0;
        // logical[0:6] ->
        // logical[7:11] -> rd[4:0]
        rd |= ((word1 >> 4) & 0x0001F);
        // logical[12:15] ->
        return new LegacyInstr.LDPI(pc, getReg(GPR_table, rd),
                LegacyRegister.X);
    }

    private LegacyInstr decode_LDPI_1(int word1) throws InvalidInstruction {
        // this method decodes LDPI when ar == Y
        int rd = 0;
        // logical[0:6] ->
        // logical[7:11] -> rd[4:0]
        rd |= ((word1 >> 4) & 0x0001F);
        // logical[12:15] ->
        return new LegacyInstr.LDPI(pc, getReg(GPR_table, rd),
                LegacyRegister.Y);
    }

    private LegacyInstr decode_ELPMD_0(int word1) throws InvalidInstruction
    {
        int rd = 0;
        int rr = 0;
        // logical[0:6] ->
        // logical[7:11] -> rd[4:0]
        rd |= ((word1 >> 4) & 0x0001F);
        // logical[12:15] ->
        return new LegacyInstr.ELPMD(pc, getReg(GPR_table, rd),
                getReg(Z_table, rr));
    }

    private LegacyInstr decode_LDPI_2(int word1) throws InvalidInstruction {
        // this method decodes LDPI when ar == Z
        int rd = 0;
        // logical[0:6] ->
        // logical[7:11] -> rd[4:0]
        rd |= ((word1 >> 4) & 0x0001F);
        // logical[12:15] ->
        return new LegacyInstr.LDPI(pc, getReg(GPR_table, rd),
                LegacyRegister.Z);
    }

    private LegacyInstr decode_LDPD_0(int word1) throws InvalidInstruction {
        // this method decodes LDPD when ar == X
        int rd = 0;
        // logical[0:6] ->
        // logical[7:11] -> rd[4:0]
        rd |= ((word1 >> 4) & 0x0001F);
        // logical[12:15] ->
        return new LegacyInstr.LDPD(pc, getReg(GPR_table, rd),
                LegacyRegister.X);
    }

    private LegacyInstr decode_LDPD_1(int word1) throws InvalidInstruction {
        // this method decodes LDPD when ar == Y
        int rd = 0;
        // logical[0:6] ->
        // logical[7:11] -> rd[4:0]
        rd |= ((word1 >> 4) & 0x0001F);
        // logical[12:15] ->
        return new LegacyInstr.LDPD(pc, getReg(GPR_table, rd),
                LegacyRegister.Y);
    }

    private LegacyInstr decode_LPMPI_0(int word1) throws InvalidInstruction
    {
        int rd = 0;
        int z = 0;
        // logical[0:6] ->
        // logical[7:11] -> rd[4:0]
        rd |= ((word1 >> 4) & 0x0001F);
        // logical[12:15] ->
        return new LegacyInstr.LPMPI(pc, getReg(GPR_table, rd),
                getReg(Z_table, z));
    }

    private LegacyInstr decode_LDS_0(int word1) throws InvalidInstruction {
        int word2 = getWord(1);
        int rd = 0;
        int addr = 0;
        // logical[0:6] ->
        // logical[7:11] -> rd[4:0]
        rd |= ((word1 >> 4) & 0x0001F);
        // logical[12:15] ->
        // logical[16:31] -> addr[15:0]
        addr |= (word2 & 0x0FFFF);
        return new LegacyInstr.LDS(pc, getReg(GPR_table, rd), addr);
    }

    private LegacyInstr decode_23(int word1) throws InvalidInstruction {
        // get value of bits logical[12:15]
        int value = (word1) & 0x0000F;
        switch (value) {
            case 0x0000F:
                return decode_POP_0(word1);
            case 0x00004:
                return decode_LPMD_0(word1);
            case 0x00007:
                return decode_ELPMPI_0(word1);
            case 0x0000C:
                return decode_LD_0(word1);
            case 0x00002:
                return decode_LDPD_2(word1);
            case 0x0000D:
                return decode_LDPI_0(word1);
            case 0x00009:
                return decode_LDPI_1(word1);
            case 0x00006:
                return decode_ELPMD_0(word1);
            case 0x00001:
                return decode_LDPI_2(word1);
            case 0x0000E:
                return decode_LDPD_0(word1);
            case 0x0000A:
                return decode_LDPD_1(word1);
            case 0x00005:
                return decode_LPMPI_0(word1);
            case 0x00000:
                return decode_LDS_0(word1);
            default:
                return null;
        }
    }

    private LegacyInstr decode_24(int word1) throws InvalidInstruction {
        // get value of bits logical[6:6]
        int value = (word1 >> 9) & 0x00001;
        switch (value) {
            case 0x00001:
                return decode_22(word1);
        case 0x00000:
            return decode_23(word1);
            default:
                return null;
        }
    }

    private LegacyInstr decode_25(int word1) throws InvalidInstruction
    {
        // get value of bits logical[4:5]
        int value = (word1 >> 10) & 0x00003;
        switch (value) {
            case 0x00002:
                return decode_10(word1);
        case 0x00001:
            return decode_21(word1);
            case 0x00003:
                return decode_MUL_0(word1);
            case 0x00000:
                return decode_24(word1);
            default:
                return null;
        }
    }

    private LegacyInstr decode_ORI_0(int word1) throws InvalidInstruction
    {
        int rd = 0;
        int imm = 0;
        // logical[0:3] ->
        // logical[4:7] -> imm[7:4]
        imm |= ((word1 >> 8) & 0x0000F) << 4;
        // logical[8:11] -> rd[3:0]
        rd |= ((word1 >> 4) & 0x0000F);
        // logical[12:15] -> imm[3:0]
        imm |= (word1 & 0x0000F);
        return new LegacyInstr.ORI(pc, getReg(HGPR_table, rd), imm);
    }

    private LegacyInstr decode_SUB_0(int word1) throws InvalidInstruction {
        int rd = 0;
        int rr = 0;
        // logical[0:5] ->
        // logical[6:6] -> rr[4:4]
        rr |= ((word1 >> 9) & 0x00001) << 4;
        // logical[7:7] -> rd[4:4]
        rd |= ((word1 >> 8) & 0x00001) << 4;
        // logical[8:11] -> rd[3:0]
        rd |= ((word1 >> 4) & 0x0000F);
        // logical[12:15] -> rr[3:0]
        rr |= (word1 & 0x0000F);
        return new LegacyInstr.SUB(pc, getReg(GPR_table, rd), getReg(GPR_table, rr));
    }

    private LegacyInstr decode_CP_0(int word1) throws InvalidInstruction {
        int rd = 0;
        int rr = 0;
        // logical[0:5] ->
        // logical[6:6] -> rr[4:4]
        rr |= ((word1 >> 9) & 0x00001) << 4;
        // logical[7:7] -> rd[4:4]
        rd |= ((word1 >> 8) & 0x00001) << 4;
        // logical[8:11] -> rd[3:0]
        rd |= ((word1 >> 4) & 0x0000F);
        // logical[12:15] -> rr[3:0]
        rr |= (word1 & 0x0000F);
        return new LegacyInstr.CP(pc, getReg(GPR_table, rd),
                getReg(GPR_table, rr));
    }

    private LegacyInstr decode_ADC_0(int word1) throws InvalidInstruction
    {
        int rd = 0;
        int rr = 0;
        // logical[0:5] ->
        // logical[6:6] -> rr[4:4]
        rr |= ((word1 >> 9) & 0x00001) << 4;
        // logical[7:7] -> rd[4:4]
        rd |= ((word1 >> 8) & 0x00001) << 4;
        // logical[8:11] -> rd[3:0]
        rd |= ((word1 >> 4) & 0x0000F);
        // logical[12:15] -> rr[3:0]
        rr |= (word1 & 0x0000F);
        return new LegacyInstr.ADC(pc, getReg(GPR_table, rd),
                getReg(GPR_table, rr));
    }

    private LegacyInstr decode_CPSE_0(int word1) throws InvalidInstruction
    {
        int rd = 0;
        int rr = 0;
        // logical[0:5] ->
        // logical[6:6] -> rr[4:4]
        rr |= ((word1 >> 9) & 0x00001) << 4;
        // logical[7:7] -> rd[4:4]
        rd |= ((word1 >> 8) & 0x00001) << 4;
        // logical[8:11] -> rd[3:0]
        rd |= ((word1 >> 4) & 0x0000F);
        // logical[12:15] -> rr[3:0]
        rr |= (word1 & 0x0000F);
        return new LegacyInstr.CPSE(pc, getReg(GPR_table, rd),
                getReg(GPR_table, rr));
    }

    private LegacyInstr decode_26(int word1) throws InvalidInstruction
    {
        // get value of bits logical[4:5]
        int value = (word1 >> 10) & 0x00003;
        switch (value) {
            case 0x00002:
                return decode_SUB_0(word1);
            case 0x00001:
                return decode_CP_0(word1);
            case 0x00003:
                return decode_ADC_0(word1);
            case 0x00000:
                return decode_CPSE_0(word1);
            default:
                return null;
        }
    }

    private LegacyInstr decode_LDI_0(int word1) throws InvalidInstruction {
        int rd = 0;
        int imm = 0;
        // logical[0:3] ->
        // logical[4:7] -> imm[7:4]
        imm |= ((word1 >> 8) & 0x0000F) << 4;
        // logical[8:11] -> rd[3:0]
        rd |= ((word1 >> 4) & 0x0000F);
        // logical[12:15] -> imm[3:0]
        imm |= (word1 & 0x0000F);
        return new LegacyInstr.LDI(pc, getReg(HGPR_table, rd), imm);
    }

    private LegacyInstr decode_SUBI_0(int word1) throws InvalidInstruction {
        int rd = 0;
        int imm = 0;
        // logical[0:3] ->
        // logical[4:7] -> imm[7:4]
        imm |= ((word1 >> 8) & 0x0000F) << 4;
        // logical[8:11] -> rd[3:0]
        rd |= ((word1 >> 4) & 0x0000F);
        // logical[12:15] -> imm[3:0]
        imm |= (word1 & 0x0000F);
        return new LegacyInstr.SUBI(pc, getReg(HGPR_table, rd), imm);
    }

    private LegacyInstr decode_SBC_0(int word1) throws InvalidInstruction {
        int rd = 0;
        int rr = 0;
        // logical[0:5] ->
        // logical[6:6] -> rr[4:4]
        rr |= ((word1 >> 9) & 0x00001) << 4;
        // logical[7:7] -> rd[4:4]
        rd |= ((word1 >> 8) & 0x00001) << 4;
        // logical[8:11] -> rd[3:0]
        rd |= ((word1 >> 4) & 0x0000F);
        // logical[12:15] -> rr[3:0]
        rr |= (word1 & 0x0000F);
        return new LegacyInstr.SBC(pc, getReg(GPR_table, rd), getReg(GPR_table, rr));
    }

    private LegacyInstr decode_CPC_0(int word1) throws InvalidInstruction {
        int rd = 0;
        int rr = 0;
        // logical[0:5] ->
        // logical[6:6] -> rr[4:4]
        rr |= ((word1 >> 9) & 0x00001) << 4;
        // logical[7:7] -> rd[4:4]
        rd |= ((word1 >> 8) & 0x00001) << 4;
        // logical[8:11] -> rd[3:0]
        rd |= ((word1 >> 4) & 0x0000F);
        // logical[12:15] -> rr[3:0]
        rr |= (word1 & 0x0000F);
        return new LegacyInstr.CPC(pc, getReg(GPR_table, rd),
                getReg(GPR_table, rr));
    }

    private LegacyInstr decode_ADD_0(int word1) throws InvalidInstruction
    {
        int rd = 0;
        int rr = 0;
        // logical[0:5] ->
        // logical[6:6] -> rr[4:4]
        rr |= ((word1 >> 9) & 0x00001) << 4;
        // logical[7:7] -> rd[4:4]
        rd |= ((word1 >> 8) & 0x00001) << 4;
        // logical[8:11] -> rd[3:0]
        rd |= ((word1 >> 4) & 0x0000F);
        // logical[12:15] -> rr[3:0]
        rr |= (word1 & 0x0000F);
        return new LegacyInstr.ADD(pc, getReg(GPR_table, rd),
                getReg(GPR_table, rr));
    }

    private LegacyInstr decode_MULS_0(int word1) throws InvalidInstruction
    {
        int rd = 0;
        int rr = 0;
        // logical[0:7] ->
        // logical[8:11] -> rd[3:0]
        rd |= ((word1 >> 4) & 0x0000F);
        // logical[12:15] -> rr[3:0]
        rr |= (word1 & 0x0000F);
        return new LegacyInstr.MULS(pc, getReg(HGPR_table, rd),
                getReg(HGPR_table, rr));
    }

    private LegacyInstr decode_MOVW_0(int word1) throws InvalidInstruction {
        int rd = 0;
        int rr = 0;
        // logical[0:7] ->
        // logical[8:11] -> rd[3:0]
        rd |= ((word1 >> 4) & 0x0000F);
        // logical[12:15] -> rr[3:0]
        rr |= (word1 & 0x0000F);
        return new LegacyInstr.MOVW(pc, getReg(EGPR_table, rd),
                getReg(EGPR_table, rr));
    }

    private LegacyInstr decode_FMULSU_0(int word1) throws InvalidInstruction
    {
        int rd = 0;
        int rr = 0;
        // logical[0:7] ->
        // logical[8:8] ->
        // logical[9:11] -> rd[2:0]
        rd |= ((word1 >> 4) & 0x00007);
        // logical[12:12] ->
        // logical[13:15] -> rr[2:0]
        rr |= (word1 & 0x00007);
        return new LegacyInstr.FMULSU(pc, getReg(MGPR_table, rd), getReg(MGPR_table, rr));
    }

    private LegacyInstr decode_FMULS_0(int word1) throws InvalidInstruction
    {
        int rd = 0;
        int rr = 0;
        // logical[0:7] ->
        // logical[8:8] ->
        // logical[9:11] -> rd[2:0]
        rd |= ((word1 >> 4) & 0x00007);
        // logical[12:12] ->
        // logical[13:15] -> rr[2:0]
        rr |= (word1 & 0x00007);
        return new LegacyInstr.FMULS(pc, getReg(MGPR_table, rd),
                getReg(MGPR_table, rr));
    }

    private LegacyInstr decode_27(int word1) throws InvalidInstruction
    {
        // get value of bits logical[12:12]
        int value = (word1 >> 3) & 0x00001;
        switch (value) {
            case 0x00001:
                return decode_FMULSU_0(word1);
            case 0x00000:
                return decode_FMULS_0(word1);
            default:
                return null;
        }
    }

    private LegacyInstr decode_FMUL_0(int word1) throws InvalidInstruction {
        int rd = 0;
        int rr = 0;
        // logical[0:7] ->
        // logical[8:8] ->
        // logical[9:11] -> rd[2:0]
        rd |= ((word1 >> 4) & 0x00007);
        // logical[12:12] ->
        // logical[13:15] -> rr[2:0]
        rr |= (word1 & 0x00007);
        return new LegacyInstr.FMUL(pc, getReg(MGPR_table, rd), getReg(MGPR_table, rr));
    }

    private LegacyInstr decode_MULSU_0(int word1) throws InvalidInstruction
    {
        int rd = 0;
        int rr = 0;
        // logical[0:7] ->
        // logical[8:8] ->
        // logical[9:11] -> rd[2:0]
        rd |= ((word1 >> 4) & 0x00007);
        // logical[12:12] ->
        // logical[13:15] -> rr[2:0]
        rr |= (word1 & 0x00007);
        return new LegacyInstr.MULSU(pc, getReg(MGPR_table, rd),
                getReg(MGPR_table, rr));
    }

    private LegacyInstr decode_28(int word1) throws InvalidInstruction
    {
        // get value of bits logical[12:12]
        int value = (word1 >> 3) & 0x00001;
        switch (value) {
            case 0x00001:
                return decode_FMUL_0(word1);
            case 0x00000:
                return decode_MULSU_0(word1);
            default:
                return null;
        }
    }

    private LegacyInstr decode_29(int word1) throws InvalidInstruction {
        // get value of bits logical[8:8]
        int value = (word1 >> 7) & 0x00001;
        switch (value) {
            case 0x00001:
                return decode_27(word1);
        case 0x00000:
            return decode_28(word1);
            default:
                return null;
        }
    }

    private LegacyInstr decode_NOP_0(int word1) throws InvalidInstruction
    {
        if ((word1 & 0x000FF) != 0x00000) {
            return null;
        }
        // logical[0:7] ->
        // logical[8:15] ->
        return new LegacyInstr.NOP(pc);
    }

    private LegacyInstr decode_30(int word1) throws InvalidInstruction {
        // get value of bits logical[6:7]
        int value = (word1 >> 8) & 0x00003;
        switch (value) {
            case 0x00002:
                return decode_MULS_0(word1);
            case 0x00001:
                return decode_MOVW_0(word1);
            case 0x00003:
                return decode_29(word1);
            case 0x00000:
                return decode_NOP_0(word1);
            default:
                return null;
        }
    }

    private LegacyInstr decode_31(int word1) throws InvalidInstruction
    {
        // get value of bits logical[4:5]
        int value = (word1 >> 10) & 0x00003;
        switch (value) {
            case 0x00002:
                return decode_SBC_0(word1);
        case 0x00001:
            return decode_CPC_0(word1);
            case 0x00003:
            return decode_ADD_0(word1);
            case 0x00000:
            return decode_30(word1);
            default:
                return null;
        }
    }

    private LegacyInstr decode_root0(int word1) throws InvalidInstruction
    {
        // get value of bits logical[0:3]
        int value = (word1 >> 12) & 0x0000F;
        switch (value) {
            case 0x0000F:
                return decode_4(word1);
        case 0x00004:
            return decode_SBCI_0(word1);
            case 0x00008:
                return decode_7(word1);
            case 0x0000B:
                return decode_8(word1);
            case 0x00003:
                return decode_CPI_0(word1);
            case 0x00007:
                return decode_ANDI_0(word1);
            case 0x0000C:
                return decode_RJMP_0(word1);
            case 0x00002:
                return decode_9(word1);
            case 0x0000D:
                return decode_RCALL_0(word1);
            case 0x00009:
                return decode_25(word1);
            case 0x00006:
                return decode_ORI_0(word1);
            case 0x00001:
                return decode_26(word1);
            case 0x0000E:
                return decode_LDI_0(word1);
            case 0x00005:
                return decode_SUBI_0(word1);
            case 0x00000:
                return decode_31(word1);
            default:
                return null;
        }
    }

    private LegacyInstr decode_STD_0(int word1) throws InvalidInstruction
    {
        int ar = 0;
        int imm = 0;
        int rr = 0;
        // logical[0:1] ->
        // logical[2:2] -> imm[5]
        imm = Arithmetic.setBit(imm, 5, Arithmetic.getBit(word1, 13));
        // logical[3:3] ->
        // logical[4:5] -> imm[4:3]
        imm |= ((word1 >> 10) & 0x00003) << 3;
        // logical[6:6] ->
        // logical[7:11] -> rr[4:0]
        rr |= ((word1 >> 4) & 0x0001F);
        // logical[12:12] -> ar[0]
        ar = Arithmetic.setBit(ar, 0, Arithmetic.getBit(word1, 3));
        // logical[13:15] -> imm[2:0]
        imm |= (word1 & 0x00007);
        return new LegacyInstr.STD(pc, getReg(YZ_table, ar), imm, getReg(GPR_table, rr));
    }

    private LegacyInstr decode_LDD_0(int word1) throws InvalidInstruction {
        int rd = 0;
        int ar = 0;
        int imm = 0;
        // logical[0:1] ->
        // logical[2:2] -> imm[5]
        imm = Arithmetic.setBit(imm, 5, Arithmetic.getBit(word1, 13));
        // logical[3:3] ->
        // logical[4:5] -> imm[4:3]
        imm |= ((word1 >> 10) & 0x00003) << 3;
        // logical[6:6] ->
        // logical[7:11] -> rd[4:0]
        rd |= ((word1 >> 4) & 0x0001F);
        // logical[12:12] -> ar[0]
        ar = Arithmetic.setBit(ar, 0, Arithmetic.getBit(word1, 3));
        // logical[13:15] -> imm[2:0]
        imm |= (word1 & 0x00007);
        return new LegacyInstr.LDD(pc, getReg(GPR_table, rd),
                getReg(YZ_table, ar), imm);
    }

    private LegacyInstr decode_32(int word1) throws InvalidInstruction {
        // get value of bits logical[6:6]
        int value = (word1 >> 9) & 0x00001;
        switch (value) {
            case 0x00001:
                return decode_STD_0(word1);
            case 0x00000:
                return decode_LDD_0(word1);
            default:
                return null;
        }
    }

    private LegacyInstr decode_33(int word1) throws InvalidInstruction {
        // get value of bits logical[3:3]
        int value = (word1 >> 12) & 0x00001;
        switch (value) {
            case 0x00000:
                return decode_32(word1);
        default:
            return null;
        }
    }

    private LegacyInstr decode_root1(int word1) throws InvalidInstruction
    {
        // get value of bits logical[0:1]
        int value = (word1 >> 14) & 0x00003;
        switch (value) {
            case 0x00002:
                return decode_33(word1);
        default:
            return null;
        }
    }

    LegacyInstr decode_root(int word1) throws InvalidInstruction
    {
        LegacyInstr i = null;
        i = decode_root0(word1);
        if (i != null) return i;
        i = decode_root1(word1);
        if (i != null) return i;
        throw new InvalidInstruction(word1, pc);
    }

    /**
     * The <code>InvalidInstruction</code> class represents an exception
     * generated by the disassembler when it is given a machine code instruction
     * that does not correspond to a well-formed instruction.
     */
    public class InvalidInstruction extends Exception {
        private static final long serialVersionUID = 1L;
        public final int pc;
        public final int word1;

        InvalidInstruction(int word1, int pc) {
            super("Invalid Instruction at " + StringUtil.addrToString(pc));
            this.pc = pc;
            this.word1 = word1;
        }
    }
    // --END DISASSEM GENERATOR--
}
