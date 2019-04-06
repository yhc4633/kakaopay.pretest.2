package com.kakaopay.pretest.persistence.repository.custom;

import com.kakaopay.pretest.persistence.entity.impl.Program;
import com.kakaopay.pretest.persistence.repository.CommonProcess;
import com.kakaopay.pretest.persistence.repository.ProgramRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<Program> findProgramListByIntroKeyword(String programIntroKeyword) {
        if (StringUtils.isEmpty(programIntroKeyword)) {
            return null;
        }

        List<Program> programList = programRepository.findAll();
        if (CollectionUtils.isNotEmpty(programList)) {
            return programList.stream().filter(program -> StringUtils.contains(program.getIntro(), programIntroKeyword)).collect(Collectors.toList());
        }

        return Collections.EMPTY_LIST;
    }
}