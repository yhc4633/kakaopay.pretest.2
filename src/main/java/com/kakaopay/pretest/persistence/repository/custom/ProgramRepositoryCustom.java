package com.kakaopay.pretest.persistence.repository.custom;

import com.kakaopay.pretest.persistence.entity.impl.Program;
import com.kakaopay.pretest.persistence.repository.CommonProcess;
import com.kakaopay.pretest.persistence.repository.ProgramRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

@Data
@Service
@AllArgsConstructor
public class ProgramRepositoryCustom implements CommonProcess<Program> {
    private final ProgramRepository programRepository;


    @Override
    public Program saveIfNotExist(Program program) {
        if (program == null) {
            return null;
        }

        Program searchedProgram = programRepository.findProgramByNameAndIntroAndDetail(program.getName(), program.getIntro(), program.getDetail());

        if (searchedProgram != null) {
            return searchedProgram;
        }

        return programRepository.save(program);
    }
}