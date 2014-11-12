package com.example.medical.pillar;

import android.content.Context;

import com.example.medical.db.Pill;
import com.example.medical.db.PillDataAccessor;
import com.example.medical.db.PillPrescriptionJoin;
import com.example.medical.db.Prescription;
import com.example.medical.db.PrescriptionDataAccessor;
import com.example.medical.db.PrescriptionPillJoinDataAccessor;

import java.util.ArrayList;

/**
 * Created by meanheffry on 11/11/14.
 */
public class DispenserCoordinator {
    private PrescriptionDataAccessor PresDA;
    private PillDataAccessor PillDA;
    private PrescriptionPillJoinDataAccessor JoinDA;

    private ArrayList<Prescription> Prescriptions;
    private ArrayList<Pill> Pills;
    private ArrayList<PillPrescriptionJoin> joins;
    private ArrayList<byte[]> rfids;




    private final byte[] rfid1 = new byte[]{0x1A, (byte)0xE2, 0x41, (byte)0xD9};

    private final byte[] rfid2 = new byte[]{0x04, (byte)0x92, 0x6E, 0x7A, 0x7A, 0x31, (byte)0x80};


    public DispenserCoordinator(Context context){
        PresDA = new PrescriptionDataAccessor(context);
        PresDA.open();
        PillDA = new PillDataAccessor(context);
        PillDA.open();
        JoinDA = new PrescriptionPillJoinDataAccessor(context);
        JoinDA.open();

        Pills =(ArrayList)PillDA.getAllPills();
        Prescriptions = (ArrayList)PresDA.getAllPrescriptions();
        rfids= getRfids();
    }


    public ArrayList<byte[]> getRfids(){
        ArrayList<byte[]> ids = new ArrayList<byte[]>();
        for(int i=0;i<Prescriptions.size();i++){
            ids.add(Prescriptions.get(i).getRfid());
        }
        return ids;
    }



    public int dispense(byte[] b){
        int i = 0;
        if(rfids.contains(b)){

        }
        return i;
    }



}
